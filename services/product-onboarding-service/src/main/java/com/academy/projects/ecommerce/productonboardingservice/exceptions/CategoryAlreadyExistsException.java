package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String categoryName, String highLevelCategory) {
        super("Category '" + categoryName + "' already exists in '" + highLevelCategory + "'!!!");
    }
}
