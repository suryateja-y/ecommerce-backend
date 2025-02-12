package com.academy.projects.ecommerce.usermanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.usermanagementservice.dtos.ActionType;
import com.academy.projects.ecommerce.usermanagementservice.models.Employee;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto implements Serializable {
    private ActionType action;
    private Employee employee;
}
