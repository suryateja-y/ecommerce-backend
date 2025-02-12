package com.academy.projects.ecommerce.paymentmanagementservice.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionGatewayResponse implements Serializable {
    private String paymentId;
    private ActionStatus actionStatus;
    private Date actedAt;
    private String reason;
}
