package com.academy.projects.ecommerce.productservice.exceptions;

import lombok.ToString;

@ToString
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String id) {
        super("Category with id '" + id + "' not found!!!");
    }
}
