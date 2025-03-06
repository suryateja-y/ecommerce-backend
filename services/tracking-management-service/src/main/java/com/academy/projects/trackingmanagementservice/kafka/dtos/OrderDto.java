package com.academy.projects.trackingmanagementservice.kafka.dtos;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto implements Serializable {
    private String orderId;
    private Date orderDate;
    private Set<OrderItem> orderItems;
    private String customerId;
    private String shippingAddressId;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private String invoiceId;
    private Action action;
}
