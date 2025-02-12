package com.academy.projects.ecommerce.notificationservice.kafka.consumers.services;

import com.academy.projects.ecommerce.notificationservice.exceptions.InvalidActionException;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.*;
import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.User;
import com.academy.projects.ecommerce.notificationservice.services.notifications.INotificationManagementService;
import com.academy.projects.ecommerce.notificationservice.services.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Service
public class PaymentManagementService {
    private final IUserService userService;
    private final INotificationManagementService notificationManagementService;

    private final Logger logger = LoggerFactory.getLogger(PaymentManagementService.class);

    @Autowired
    public PaymentManagementService(IUserService userService, INotificationManagementService notificationManagementService) {
        this.userService = userService;
        this.notificationManagementService = notificationManagementService;
    }
    @KafkaListener(topics = "${application.kafka.topics.payment-topic}", groupId = "${application.kafka.consumer.payment-group}", containerFactory = "kafkaListenerContainerFactoryForPayment")
    public void consumer(PaymentDto paymentDto) {
        try {
            User customer = userService.getByUserid(paymentDto.getPayment().getCustomerId());
            String registryKey = getRegistryKey(paymentDto.getAction(), paymentDto.getPayment().getPaymentStatus());
            RecipientCommunicationDetails communicationDetails = getCommunicationDetails(customer);
            notificationManagementService.send(registryKey, prepareData(paymentDto, customer), communicationDetails);
            logger.info("Notification has been sent related to Payment: '{}'", paymentDto.getPayment().getPaymentId());
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getRegistryKey(Action action, PaymentStatus paymentStatus) {
        if(action.equals(Action.CREATE)) return "payment-initiate";
        else if(action.equals(Action.STATUS_UPDATE)) {
            return switch (paymentStatus) {
                case PAID -> "payment-success";
                case REFUNDED -> "payment-refund";
                default -> "payment-status";
            };
        } else throw new InvalidActionException(action);
    }

    private RecipientCommunicationDetails getCommunicationDetails(User customer) {
        return RecipientCommunicationDetails.builder()
                .email(customer.getEmail())
                .build();
    }

    private Map<String, Object> prepareData(PaymentDto paymentDto, User customer) {
        Payment payment = paymentDto.getPayment();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userName", customer.getFullName());
        data.put("orderId", payment.getOrderId());
        data.put("paymentId", payment.getPaymentId());
        data.put("total", formatCurrency(payment.getTotalAmount(), payment.getCurrency()));
        data.put("startDate", payment.getCreatedAt());
        data.put("paymentUrl", payment.getPaymentUrl());
        data.put("expirationTime", getExpirationTime());
        data.put("refundDate", payment.getModifiedAt());
        data.put("paymentStatus", payment.getPaymentStatus());
        data.put("reason", payment.getReason());
        return data;
    }

    private String formatCurrency(BigDecimal amount, CurrencyType currencyType) {
        Locale locale = getLocale(currencyType);
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return format.format(amount);
    }

    private Locale getLocale(CurrencyType currencyType) {
        return (Objects.requireNonNull(currencyType) == CurrencyType.INR) ? new Locale("en", "IN") : Locale.US;
    }

    private Date getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 10);
        return calendar.getTime();
    }
}
