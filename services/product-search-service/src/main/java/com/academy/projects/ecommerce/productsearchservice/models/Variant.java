package com.academy.projects.ecommerce.productsearchservice.models;

import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Variant {
    private String productId;
    private String variantId;
    private Map<String, String> variantAttributes;
    private DeliveryDetails deliveryDetails;
}
