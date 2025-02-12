package com.academy.projects.ecommerce.ordermanagementservice.dtos;

import com.academy.projects.ecommerce.ordermanagementservice.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerOptionsRequestDto implements Serializable {
    private String variantId;
    private Address userAddress;
}
