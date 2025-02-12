package com.academy.projects.ecommerce.usermanagementservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "sellers")
@Getter
@Setter
@RequiredArgsConstructor
public class Seller extends IDBaseModel {
    private String companyName;
    private String brandName;

    @OneToOne
    private Address address;

    @ManyToOne
    private User user;

    private String approvalId;
}
