package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import com.academy.projects.ecommerce.notificationservice.models.User;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee implements Serializable {
    private String id;
    private String employeeId;
    private String designation;
    private String bloodGroup;
    private User user;
    private String approvalId;
    private Address address;
}
