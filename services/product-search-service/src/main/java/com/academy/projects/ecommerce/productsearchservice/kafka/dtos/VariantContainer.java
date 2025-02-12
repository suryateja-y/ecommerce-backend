package com.academy.projects.ecommerce.productsearchservice.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariantContainer implements Serializable {
    private String productId;
    private String variantId;
    private List<AttributeDto> attributes;
}
