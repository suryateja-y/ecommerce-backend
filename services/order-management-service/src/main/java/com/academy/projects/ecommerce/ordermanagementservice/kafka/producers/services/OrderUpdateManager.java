package com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.services;

import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.OrderDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderUpdateManager implements IOrderUpdateManager {
    @Value("${application.kafka.topics.order-topic}")
    private String orderTopic;

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;

    @Autowired
    public OrderUpdateManager(KafkaTemplate<String, OrderDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendUpdate(Order order, Action action) {
        kafkaTemplate.send(orderTopic, from(order, action));
    }

    private OrderDto from(Order order, Action action) {
        return OrderDto.builder()
                .orderId(order.getId())
                .orderDate(order.getCreatedAt())
                .orderItems(order.getOrderItems())
                .orderStatus(order.getOrderStatus())
                .customerId(order.getCustomerId())
                .totalAmount(order.getTotalAmount())
                .invoiceId(order.getInvoice() == null ? "" : order.getInvoice().getId())
                .action(action)
                .build();
    }
}
