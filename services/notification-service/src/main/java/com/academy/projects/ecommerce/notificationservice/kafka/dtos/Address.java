package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {
    private String id;
    private String houseNumber;
    private String street;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zip;
    private String country;
    private AddressType addressType;
}
