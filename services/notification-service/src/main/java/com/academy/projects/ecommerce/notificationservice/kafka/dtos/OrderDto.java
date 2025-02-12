package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Serializable {
    private String orderId;
    private Date orderDate;
    private Set<OrderItem> orderItems;
    private String customerId;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private String invoiceId;
    private CurrencyType currencyType;
    private Action action;
}
