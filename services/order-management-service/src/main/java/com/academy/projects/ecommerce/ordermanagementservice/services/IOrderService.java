package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.UpdateOrderRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import com.academy.projects.ecommerce.ordermanagementservice.models.OrderStatus;
import com.academy.projects.ecommerce.ordermanagementservice.models.PaymentStatus;
import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrder;

import java.util.List;

public interface IOrderService {
    List<Order> getOrders(String customerId, OrderStatus orderStatus, int page, int pageSize);
    Order updateStatus(String customerId, String orderId, OrderStatus orderStatus);
    Order updateStatus(String customerId, String orderId, PaymentStatus paymentStatus);
    PreOrder createOrders(PreOrder preOrder);
}
