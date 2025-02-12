package com.academy.projects.ecommerce.usermanagementservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "employees")
@Getter
@Setter
@RequiredArgsConstructor
public class Employee extends IDBaseModel {
    @CustomUpdate
    private String employeeId;

    @CustomUpdate
    private String designation;

    private Integer age;
    private Gender gender;
    private String bloodGroup;

    @OneToOne
    private Address address;

    @ManyToOne
    private User user;

    private String approvalId;
}
