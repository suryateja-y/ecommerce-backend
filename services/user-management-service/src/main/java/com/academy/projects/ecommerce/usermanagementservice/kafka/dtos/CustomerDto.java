package com.academy.projects.ecommerce.usermanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.usermanagementservice.dtos.ActionType;
import com.academy.projects.ecommerce.usermanagementservice.models.Customer;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto implements Serializable {
    private ActionType action;
    private Customer customer;
}
