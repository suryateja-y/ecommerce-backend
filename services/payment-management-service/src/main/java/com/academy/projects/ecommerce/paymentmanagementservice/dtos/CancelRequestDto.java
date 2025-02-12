package com.academy.projects.ecommerce.paymentmanagementservice.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelRequestDto implements Serializable {
    private String paymentId;
    private String reason;
}
