package com.academy.projects.ecommerce.productsearchservice.dtos;

import com.academy.projects.ecommerce.productsearchservice.models.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryFeasibilityRequestDto implements Serializable {
    private List<DeliveryFeasibilityItem> items;
    private Address customerAddress;
}
