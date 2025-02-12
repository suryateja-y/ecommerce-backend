package com.academy.projects.ecommerce.productsearchservice.exceptions;

import lombok.ToString;

@ToString
public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(String variantId) {
        super("Variant '" + variantId + "' not found!!!");
    }
}
