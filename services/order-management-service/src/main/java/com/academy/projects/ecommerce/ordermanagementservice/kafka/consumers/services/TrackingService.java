package com.academy.projects.ecommerce.ordermanagementservice.kafka.consumers.services;

import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.TrackingDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import com.academy.projects.ecommerce.ordermanagementservice.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TrackingService {
    private final OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(TrackingService.class);

    @Autowired
    public TrackingService(OrderService orderService) {
        this.orderService = orderService;
    }
    @KafkaListener(topics = "${application.kafka.topics.tracking-topic}", groupId = "${application.kafka.consumer.tracking-group}", containerFactory = "kafkaListenerContainerFactoryForTracking")
    public void consumer(TrackingDto trackingDto) {
        try {
            Order order = orderService.updateTracking(trackingDto);
            if(order != null)
                logger.info("Order: '{}' Tracking details updated!!!", order.getId());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
