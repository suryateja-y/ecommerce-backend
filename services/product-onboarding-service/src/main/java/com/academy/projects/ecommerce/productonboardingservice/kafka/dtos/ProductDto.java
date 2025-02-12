package com.academy.projects.ecommerce.productonboardingservice.kafka.dtos;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.models.Product;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto implements Serializable {
    private Product product;
    private String sellerId;
    private ActionType action;
}
