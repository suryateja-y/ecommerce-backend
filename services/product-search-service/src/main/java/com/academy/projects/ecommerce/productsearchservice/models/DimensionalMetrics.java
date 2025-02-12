package com.academy.projects.ecommerce.productsearchservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DimensionalMetrics extends BaseModel {
    private Dimension dimension;
    private Double weight;
}
