package com.academy.projects.ecommerce.ordermanagementservice.dtos;

import com.academy.projects.ecommerce.ordermanagementservice.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponseDto implements Serializable {
    private String orderId;
    private OrderStatus orderStatus;
}
