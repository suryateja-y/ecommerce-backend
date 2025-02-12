package com.academy.projects.ecommerce.ordermanagementservice.dtos;

import com.academy.projects.ecommerce.ordermanagementservice.models.OrderStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderStatusRequestDto implements Serializable {
    private String orderId;
    private OrderStatus orderStatus;
}
