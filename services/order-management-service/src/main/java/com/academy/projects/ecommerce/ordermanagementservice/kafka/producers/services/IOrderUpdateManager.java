package com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.services;

import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrder;

public interface IOrderUpdateManager {
    void sendUpdate(Order order, Action action);
    void sendUpdate(PreOrder preOrder, Action action);
}
