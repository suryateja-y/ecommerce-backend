package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import com.academy.projects.ecommerce.notificationservice.models.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class Seller implements Serializable {
    private String id;
    private String companyName;
    private String brandName;
    private User user;
    private String approvalId;
    private Address address;
}
