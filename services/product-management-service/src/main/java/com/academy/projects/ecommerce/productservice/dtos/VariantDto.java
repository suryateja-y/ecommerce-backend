package com.academy.projects.ecommerce.productservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariantDto implements Serializable {
    private VariantWrapper variant;
    private DeliveryDetails deliveryDetails;
}
