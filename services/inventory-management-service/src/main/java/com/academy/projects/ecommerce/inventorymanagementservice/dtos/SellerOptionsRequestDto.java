package com.academy.projects.ecommerce.inventorymanagementservice.dtos;

import com.academy.projects.ecommerce.inventorymanagementservice.models.Address;
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
