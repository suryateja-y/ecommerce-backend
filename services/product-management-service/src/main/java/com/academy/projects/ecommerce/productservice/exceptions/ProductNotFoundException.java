package com.academy.projects.ecommerce.productservice.exceptions;

import lombok.ToString;

@ToString
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super("Product with id '" + id + "' not found!!!");
    }
}
