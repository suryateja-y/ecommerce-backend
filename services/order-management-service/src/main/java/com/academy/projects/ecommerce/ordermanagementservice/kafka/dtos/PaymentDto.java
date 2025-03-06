package com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto implements Serializable {
    private Payment payment;
    private Action action;
    private ActionStatus actionStatus;
    private String message;
}
