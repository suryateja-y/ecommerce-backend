package com.academy.projects.ecommerce.cartmanagementservice.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Serializable {
    private String orderId;
    private Date orderDate;
    private String customerId;
    private BigDecimal totalAmount;
}
