package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.UpdateOrderRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrder;
import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrderStatus;

import java.util.List;

public interface IPreOrderService {
    PreOrder checkout(String customerId, String addressId);
    PreOrder update(UpdateOrderRequestDto requestDto);
    List<PreOrder> getOrders(String customerId, PreOrderStatus preOrderStatus, int page, int pageSize);
    PreOrder getOrNull(String orderId);
}
