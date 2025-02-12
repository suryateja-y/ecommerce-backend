package com.academy.projects.ecommerce.productsearchservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilters implements Serializable {
    private List<String> productId;
    private List<String> name;
    private List<String> company;
    private List<String> brandName;
    private List<Integer> length;
    private List<Integer> width;
    private List<Integer> height;
    private List<Double> weight;
    private List<String> category;
    private List<String> highLevelCategory;
    private Map<String, List<String>> attributes;
    private Map<String, List<String>> variantAttributes;
}
