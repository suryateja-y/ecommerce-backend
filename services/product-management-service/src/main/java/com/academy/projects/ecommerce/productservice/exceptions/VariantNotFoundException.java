package com.academy.projects.ecommerce.productservice.exceptions;

import lombok.ToString;

@ToString
public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(String productId, String variantId) {
        super("Variant '" + variantId + "' not found in product '" + productId + "'");
    }
}
