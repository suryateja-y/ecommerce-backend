package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeUpdateDto implements Serializable {
    private Action action;
    private Employee employee;
}
