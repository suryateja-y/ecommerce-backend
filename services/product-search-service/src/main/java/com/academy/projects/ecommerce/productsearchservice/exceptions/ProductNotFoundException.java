package com.academy.projects.ecommerce.productsearchservice.exceptions;

import lombok.ToString;

@ToString
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("Product with id '" + productId + "' not found!!!");
    }
}
