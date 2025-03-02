package com.academy.projects.ecommerce.ordermanagementservice.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryFeasibilityDetails implements Serializable {
    private Date eta;
    private long etaInHours;
    private Boolean isFeasible = false;
    private String reason;
    private String sellerId;
}
