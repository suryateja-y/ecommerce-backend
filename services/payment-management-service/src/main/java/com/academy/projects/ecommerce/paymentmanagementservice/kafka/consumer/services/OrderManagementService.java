package com.academy.projects.ecommerce.paymentmanagementservice.kafka.consumer.services;

import com.academy.projects.ecommerce.paymentmanagementservice.dtos.PaymentInitiationRequest;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.*;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.producers.services.PaymentUpdateManager;
import com.academy.projects.ecommerce.paymentmanagementservice.models.Payment;
import com.academy.projects.ecommerce.paymentmanagementservice.services.IPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderManagementService {

    private final IPaymentService paymentService;
    private final PaymentUpdateManager paymentUpdateManager;

    private final Logger logger = LoggerFactory.getLogger(OrderManagementService.class);

    @Autowired
    public OrderManagementService(IPaymentService paymentService, PaymentUpdateManager paymentUpdateManager) {
        this.paymentService = paymentService;
        this.paymentUpdateManager = paymentUpdateManager;
    }

    @KafkaListener(topics = "${application.kafka.topics.pre-order-topic}", groupId = "${application.kafka.consumer.pre-order-group}", containerFactory = "kafkaListenerContainerFactoryForPreOrder")
    public void consumer(PreOrderDto preOrderDto) {
        try {
            switch (preOrderDto.getOrderStatus()) {
                case PENDING_FOR_PAYMENT: initiatePayment(preOrderDto); break;
                case CANCELLED: cancelPayment(preOrderDto); break;
                case REFUND: refundPayment(preOrderDto); break;
            }


        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.order-topic}", groupId = "${application.kafka.consumer.order-group}", containerFactory = "kafkaListenerContainerFactoryForOrder")
    public void consumer(OrderDto orderDto) {
        PaymentDto paymentDto = new PaymentDto();
        Payment payment = null;
        try {
            if(orderDto.getAction().equals(Action.CANCEL_REQUESTED)) {
                payment = paymentService.refund(from(orderDto));
                payment.setOrderId(orderDto.getOrderId());
                payment = paymentService.refund(from(orderDto));
                paymentDto.setPayment(payment);
                paymentDto.setAction(Action.CANCEL_REQUESTED);
                paymentDto.setActionStatus(ActionStatus.SUCCEEDED);
                paymentDto.setMessage("Refund initiated for the Order");
                logger.info("Payment Refund Successful for the Order!!! PaymentId: '{}'", payment.getPaymentId());
            }
        } catch(Exception e) {
            paymentDto.setActionStatus(ActionStatus.FAILED);
            paymentDto.setAction(Action.CANCEL_REQUESTED);
            paymentDto.setMessage(e.getMessage());
            paymentDto.setPayment(payment);
        }
        // Sending update to Kafka
        paymentUpdateManager.sendUpdate(paymentDto);

    }

    private void initiatePayment(PreOrderDto preOrderDto) {
        Payment payment = paymentService.initiatePayment(from(preOrderDto));
        // Send mail to the user and the Order and Payment manager
        paymentUpdateManager.sendUpdate(payment, Action.CREATE);
        logger.info("Payment Initiation Successful!!! PaymentId: '{}'", payment.getPaymentId());
    }

    private void cancelPayment(PreOrderDto preOrderDto) {
        Payment payment = paymentService.cancel(from(preOrderDto));
        // Sending update to Kafka
        paymentUpdateManager.sendUpdate(payment, Action.STATUS_UPDATE);
        logger.info("Payment Cancellation Successful!!! PaymentId: '{}'", payment.getPaymentId());
    }

    private void refundPayment(PreOrderDto preOrderDto) {
        Payment payment = paymentService.refund(from(preOrderDto));
        // Sending update to Kafka
        paymentUpdateManager.sendUpdate(payment, Action.STATUS_UPDATE);
        logger.info("Payment Refund Successful!!! PaymentId: '{}'", payment.getPaymentId());
    }

    private PaymentInitiationRequest from(PreOrderDto preOrderDto) {
        return PaymentInitiationRequest.builder()
                .orderId(preOrderDto.getPreOrderId())
                .customerId(preOrderDto.getCustomerId())
                .orderItems(preOrderDto.getPreOrderItems())
                .build();
    }

    private PaymentInitiationRequest from(OrderDto orderDto) {
        return PaymentInitiationRequest.builder()
                .orderId(orderDto.getPreOrderId())
                .customerId(orderDto.getCustomerId())
                .orderItems(Set.of())
                .build();
    }
}
