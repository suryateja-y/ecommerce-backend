package com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.services;

import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.OrderDto;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.PreOrderDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderUpdateManager implements IOrderUpdateManager {
    @Value("${application.kafka.topics.pre-order-topic}")
    private String preOrderTopic;

    @Value("${application.kafka.topics.order-topic}")
    private String orderTopic;

    private final KafkaTemplate<String, OrderDto> kafkaTemplate;
    private final KafkaTemplate<String, PreOrderDto> preOrderKafkaTemplate;

    @Autowired
    public OrderUpdateManager(KafkaTemplate<String, OrderDto> kafkaTemplate, KafkaTemplate<String, PreOrderDto> preOrderKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.preOrderKafkaTemplate = preOrderKafkaTemplate;
    }

    @Override
    public void sendUpdate(Order order, Action action) {
        kafkaTemplate.send(orderTopic, from(order, action));
    }

    @Override
    public void sendUpdate(PreOrder preOrder, Action action) {
        preOrderKafkaTemplate.send(preOrderTopic, from(preOrder, action));
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

    private PreOrderDto from(PreOrder preOrder, Action action) {
        return PreOrderDto.builder()
                .preOrderId(preOrder.getId())
                .preOrderDate(preOrder.getCreatedAt())
                .preOrderItems(preOrder.getPreOrderItems())
                .orderStatus(preOrder.getOrderStatus())
                .customerId(preOrder.getCustomerId())
                .totalAmount(preOrder.getTotalAmount())
                .invoiceId(preOrder.getInvoice() == null ? "" : preOrder.getInvoice().getId())
                .action(action)
                .build();
    }
}
