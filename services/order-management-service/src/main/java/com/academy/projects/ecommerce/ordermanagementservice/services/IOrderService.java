package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.PaymentDto;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.TrackingDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import com.academy.projects.ecommerce.ordermanagementservice.models.OrderStatus;
import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrder;

import java.util.List;

public interface IOrderService {
    List<Order> getOrders(String customerId, OrderStatus orderStatus, int page, int pageSize);
    Order updateStatus(String customerId, String orderId, OrderStatus orderStatus);
    PreOrder createOrders(PreOrder preOrder);
    Order updateTracking(TrackingDto trackingDto);
    Order updatePayment(PaymentDto paymentDto);
}
