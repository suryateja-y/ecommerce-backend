package com.academy.projects.ecommerce.ordermanagementservice.dtos;

import com.academy.projects.ecommerce.ordermanagementservice.models.PaymentDetails;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderRequestDto implements Serializable {
    private String orderId;
    private String customerId;
    private PaymentDetails paymentDetails;
}
