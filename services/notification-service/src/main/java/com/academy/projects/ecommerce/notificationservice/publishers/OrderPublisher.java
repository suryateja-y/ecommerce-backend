package com.academy.projects.ecommerce.notificationservice.publishers;

import com.academy.projects.ecommerce.notificationservice.kafka.dtos.OrderDto;
import com.academy.projects.ecommerce.notificationservice.observers.OrderObserver;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class OrderPublisher {
    private final List<OrderObserver> orderObservers = new LinkedList<>();

    public void addObserver(OrderObserver observer) {
        orderObservers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        orderObservers.remove(observer);
    }
    public void publish(OrderDto orderDto) {
        for (OrderObserver observer : orderObservers) {
            observer.orderUpdate(orderDto);
        }
    }




}
