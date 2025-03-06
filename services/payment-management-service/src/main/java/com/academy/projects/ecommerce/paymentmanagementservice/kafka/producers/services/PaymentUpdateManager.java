package com.academy.projects.ecommerce.paymentmanagementservice.kafka.producers.services;

import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.PaymentDto;
import com.academy.projects.ecommerce.paymentmanagementservice.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentUpdateManager {
    @Value("${application.kafka.topics.payment-topic}")
    private String paymentTopic;

    private final KafkaTemplate<String, PaymentDto> kafkaTemplate;

    @Autowired
    public PaymentUpdateManager(KafkaTemplate<String, PaymentDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUpdate(PaymentDto paymentDto) {
        kafkaTemplate.send(paymentTopic, paymentDto);
    }

    public void sendUpdate(Payment payment, Action action) {
        PaymentDto paymentDto = PaymentDto.builder().payment(payment).action(action).build();
        kafkaTemplate.send(paymentTopic, paymentDto);
    }
}
