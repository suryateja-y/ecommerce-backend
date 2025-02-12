package com.academy.projects.ecommerce.productonboardingservice.dtos;

import com.academy.projects.ecommerce.productonboardingservice.models.Attribute;
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
public class CreateVariantRequestDto implements Serializable {
    private String productId;
    private List<Attribute> variantAttributes;
}
