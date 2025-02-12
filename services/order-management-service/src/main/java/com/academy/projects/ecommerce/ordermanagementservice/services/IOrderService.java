package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.UpdateOrderRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import com.academy.projects.ecommerce.ordermanagementservice.models.OrderStatus;

import java.util.List;

public interface IOrderService {
    Order checkout(String customerId);
    List<Order> getOrders(String customerId, OrderStatus orderStatus, int page, int pageSize);
    Order update(UpdateOrderRequestDto requestDto);
    Order updateStatus(String customerId, String orderId, OrderStatus orderStatus);
}
