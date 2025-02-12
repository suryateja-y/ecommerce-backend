package com.academy.projects.ecommerce.ordermanagementservice.clients.services;

import com.academy.projects.ecommerce.ordermanagementservice.models.OrderItem;

import java.util.Set;

public interface ICartToOrderService {
    Set<OrderItem> getOrderItems();
}
