package com.academy.projects.ecommerce.paymentmanagementservice.kafka.consumer.services;

import com.academy.projects.ecommerce.paymentmanagementservice.dtos.PaymentInitiationRequest;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.OrderDto;
import com.academy.projects.ecommerce.paymentmanagementservice.models.Payment;
import com.academy.projects.ecommerce.paymentmanagementservice.services.IPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderManagementService {

    private final IPaymentService paymentService;

    private final Logger logger = LoggerFactory.getLogger(OrderManagementService.class);

    @Autowired
    public OrderManagementService(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "${application.kafka.topics.order-topic}", groupId = "${application.kafka.consumer.order-group}", containerFactory = "kafkaListenerContainerFactoryForOrder")
    public void consumer(OrderDto orderDto) {
        try {
            switch (orderDto.getOrderStatus()) {
                case PENDING_FOR_PAYMENT: initiatePayment(orderDto); break;
                case CANCELLED: cancelPayment(orderDto); break;
                case REFUND: refundPayment(orderDto); break;
            }


        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initiatePayment(OrderDto orderDto) {
        Payment payment = paymentService.initiatePayment(from(orderDto));
        logger.info("Payment Initiation Successful!!! PaymentId: '{}'", payment.getPaymentId());
    }

    private void cancelPayment(OrderDto orderDto) {
        Payment payment = paymentService.cancel(from(orderDto));
        logger.info("Payment Cancellation Successful!!! PaymentId: '{}'", payment.getPaymentId());
    }

    private void refundPayment(OrderDto orderDto) {
        Payment payment = paymentService.refund(from(orderDto));
        logger.info("Payment Refund Successful!!! PaymentId: '{}'", payment.getPaymentId());
    }

    private PaymentInitiationRequest from(OrderDto orderDto) {
        return PaymentInitiationRequest.builder()
                .orderId(orderDto.getOrderId())
                .customerId(orderDto.getCustomerId())
                .orderItems(orderDto.getOrderItems())
                .build();
    }
}
