package com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos;

public enum OrderStatus {
    PENDING_FOR_PAYMENT,
    CREATED,
    CANCELLED,
    COMPLETED,
    REFUND
}
