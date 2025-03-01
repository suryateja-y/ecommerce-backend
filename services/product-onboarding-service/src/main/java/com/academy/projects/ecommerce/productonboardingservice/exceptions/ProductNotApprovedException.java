package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class ProductNotApprovedException extends RuntimeException {
    public ProductNotApprovedException(String productId) {
        super("Product '" + productId + "' is not approved yet!!!");
    }
}
