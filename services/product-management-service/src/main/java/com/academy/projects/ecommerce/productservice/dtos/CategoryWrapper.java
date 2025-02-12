package com.academy.projects.ecommerce.productservice.dtos;

import com.academy.projects.ecommerce.productservice.models.HighLevelCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryWrapper extends HighLevelCategory implements Serializable {
    private String categoryName;
    private String categoryDescription;

}
