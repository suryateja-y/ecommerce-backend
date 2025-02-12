package com.academy.projects.ecommerce.ordermanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDetailsRequestDto implements Serializable {
    private String variantId;
    private String sellerId;
}
