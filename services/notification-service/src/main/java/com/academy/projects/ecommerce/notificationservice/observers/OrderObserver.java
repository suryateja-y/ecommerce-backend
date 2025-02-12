package com.academy.projects.ecommerce.notificationservice.observers;

import com.academy.projects.ecommerce.notificationservice.kafka.dtos.OrderDto;

public interface OrderObserver {
    void orderUpdate(OrderDto order);
}
