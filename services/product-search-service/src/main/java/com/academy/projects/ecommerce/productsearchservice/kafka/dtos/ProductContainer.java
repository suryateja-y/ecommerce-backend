package com.academy.projects.ecommerce.productsearchservice.kafka.dtos;

import com.academy.projects.ecommerce.productsearchservice.models.Category;
import com.academy.projects.ecommerce.productsearchservice.models.DimensionalMetrics;
import com.academy.projects.ecommerce.productsearchservice.models.Image;
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
public class ProductContainer implements Serializable {
    private String id;
    private String name;
    private String description;
    private String company;
    private String brandName;
    private List<Image> image;
    private DimensionalMetrics dimensionalMetrics;
    private Category category;
    private List<AttributeDto> attributes;
}
