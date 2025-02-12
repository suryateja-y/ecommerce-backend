package com.academy.projects.ecommerce.productservice.dtos;

import com.academy.projects.ecommerce.productservice.models.Attribute;
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
public class VariantWrapper implements Serializable {
    private List<Attribute> attributes;
}
