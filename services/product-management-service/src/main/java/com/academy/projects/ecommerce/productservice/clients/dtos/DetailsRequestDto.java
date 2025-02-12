package com.academy.projects.ecommerce.productservice.clients.dtos;

import com.academy.projects.ecommerce.productservice.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsRequestDto implements Serializable {
    private String productId;
    private String variantId;
    private String sellerId;
    private Address userAddress;
}
