package com.academy.projects.ecommerce.productsearchservice.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class Category extends HighLevelCategory implements Serializable {

    @NotBlank(message = "Category name should be specified!!!")
    private String categoryName;

    @NotBlank(message = "Category description should be mentioned!!!")
    private String categoryDescription;

}
