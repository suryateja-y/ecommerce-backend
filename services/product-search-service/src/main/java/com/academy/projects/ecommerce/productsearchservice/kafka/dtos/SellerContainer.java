package com.academy.projects.ecommerce.productsearchservice.kafka.dtos;

import com.academy.projects.ecommerce.productsearchservice.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerContainer implements Serializable {
    private String id;
    private String companyName;
    private String brandName;
    private Address address;
    private UserDto user;
}
