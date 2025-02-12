package com.academy.projects.ecommerce.productsearchservice.kafka.dtos;

import com.academy.projects.ecommerce.productsearchservice.models.Variant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariantActionDto implements Serializable {
    private Variant variant;
    private Action action;
}
