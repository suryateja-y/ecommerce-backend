package com.academy.projects.ecommerce.productservice.kafka.dtos;

import com.academy.projects.ecommerce.productservice.models.Variant;
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
    private Variant variant;
    private Action action;
}
