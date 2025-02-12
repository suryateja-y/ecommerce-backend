package com.academy.projects.ecommerce.productsearchservice.dtos;

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
public class ProductSearchDto implements Serializable {
    private String query;
    private ProductFilters filters;
    private Address userAddress;
}
