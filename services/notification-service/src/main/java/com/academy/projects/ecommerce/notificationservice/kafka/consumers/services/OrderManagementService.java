package com.academy.projects.ecommerce.notificationservice.kafka.consumers.services;

import com.academy.projects.ecommerce.notificationservice.kafka.dtos.CurrencyType;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.OrderDto;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.OrderItem;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.OrderStatus;
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
public class OrderManagementService {
    private final IUserService userService;
    private final INotificationManagementService notificationManagementService;

    private final Logger logger = LoggerFactory.getLogger(OrderManagementService.class);

    @Autowired
    public OrderManagementService(IUserService userService, INotificationManagementService notificationManagementService) {
        this.userService = userService;
        this.notificationManagementService = notificationManagementService;
    }
    @KafkaListener(topics = "${application.kafka.topics.order-topic}", groupId = "${application.kafka.consumer.order-group}", containerFactory = "kafkaListenerContainerFactoryForOrder")
    public void consumer(OrderDto orderDto) {
        try {
            User customer = userService.getByUserid(orderDto.getCustomerId());
            String registryKey = getRegistryKey(orderDto.getOrderStatus());
            RecipientCommunicationDetails communicationDetails = getCommunicationDetails(customer);
            notificationManagementService.send(registryKey, prepareData(orderDto, customer), communicationDetails);
            logger.info("Notification has been sent related to the Order: '{}'", orderDto.getOrderId());
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getRegistryKey(OrderStatus orderStatus) {
        return orderStatus.equals(OrderStatus.CREATED) ? "order-create" : "order-status";
    }

    private RecipientCommunicationDetails getCommunicationDetails(User customer) {
        return RecipientCommunicationDetails.builder()
                .email(customer.getEmail())
                .build();
    }

    private Map<String, Object> prepareData(OrderDto orderDto, User customer) {
        update(orderDto.getOrderItems(), orderDto);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userName", customer.getFullName());
        data.put("orderId", orderDto.getOrderId());
        data.put("orderDate", orderDto.getOrderDate());
        data.put("orderItems", orderDto.getOrderItems());
        data.put("total", formatCurrency(orderDto.getTotalAmount(), orderDto.getCurrencyType()));
        data.put("invoiceId", orderDto.getInvoiceId());
        data.put("trackingId", getTrackingId());
        data.put("orderStatus", orderDto.getOrderStatus());
        return data;
    }

    private String getTrackingId() {
        return UUID.randomUUID().toString();
    }

    private String formatCurrency(BigDecimal amount, CurrencyType currencyType) {
        Locale locale = getLocale(currencyType);
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return format.format(amount);
    }

    private Locale getLocale(CurrencyType currencyType) {
        return (Objects.requireNonNull(currencyType) == CurrencyType.INR) ? new Locale("en", "IN") : Locale.US;
    }

    private void update(Set<OrderItem> orderItems, OrderDto orderDto) {
        for(OrderItem orderItem : orderItems) {
            orderItem.setTotalPriceString(formatCurrency(orderItem.getTotalPrice(), orderDto.getCurrencyType()));
            orderItem.setUnitPriceString(formatCurrency(orderItem.getUnitPrice(), orderDto.getCurrencyType()));
        }
    }

}
