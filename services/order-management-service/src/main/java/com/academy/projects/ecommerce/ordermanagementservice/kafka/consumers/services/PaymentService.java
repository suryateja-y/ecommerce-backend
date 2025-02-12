package com.academy.projects.ecommerce.ordermanagementservice.kafka.consumers.services;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.UpdateOrderRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.Payment;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.PaymentDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import com.academy.projects.ecommerce.ordermanagementservice.models.PaymentDetails;
import com.academy.projects.ecommerce.ordermanagementservice.services.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final IOrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    public PaymentService(IOrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${application.kafka.topics.payment-topic}", groupId = "${application.kafka.consumer.payment-group}", containerFactory = "kafkaListenerContainerFactoryForPayment")
    public void consumer(PaymentDto paymentDto) {
        try {
            Order order = orderService.update(from(paymentDto.getPayment()));
            logger.info("Order updated: '{}'!!!", order.getId());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UpdateOrderRequestDto from(Payment payment) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setPaymentId(payment.getPaymentId());
        paymentDetails.setPaymentMethod(payment.getPaymentMethod());
        paymentDetails.setPaymentDate(payment.getCreatedAt());
        paymentDetails.setPaymentStatus(payment.getPaymentStatus());

        return UpdateOrderRequestDto.builder()
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .paymentDetails(paymentDetails)
                .build();
    }
}
