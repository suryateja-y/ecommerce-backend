package com.academy.projects.ecommerce.cartmanagementservice.kafka.consumer.services;

import com.academy.projects.ecommerce.cartmanagementservice.kafka.dtos.OrderDto;
import com.academy.projects.ecommerce.cartmanagementservice.services.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    private final ICartService cartService;

    @Autowired
    public OrderConsumer(ICartService cartService) {
        this.cartService = cartService;
    }

    @KafkaListener(topics = "${application.kafka.topics.order-topic}", groupId = "${application.kafka.consumer.order-consumer}", containerFactory = "kafkaListenerContainerFactory")
    public void consumer(OrderDto orderDto) {
        if((orderDto != null) && (orderDto.getOrderId() != null))
            cartService.clear(orderDto.getCustomerId());
    }
}
