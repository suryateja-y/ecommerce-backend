package com.academy.projects.ecommerce.productservice.dtos;

import com.academy.projects.ecommerce.productservice.models.Attribute;
import com.academy.projects.ecommerce.productservice.models.DimensionalMetrics;
import com.academy.projects.ecommerce.productservice.models.Image;
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
public class DetailedProductDto implements Serializable {
    private String name;
    private String description;
    private String company;
    private String brandName;
    private List<Image> image;
    private DimensionalMetrics dimensionalMetrics;
    private CategoryWrapper category;
    private List<Attribute> attributes;
    private List<VariantDto> variants;
}
