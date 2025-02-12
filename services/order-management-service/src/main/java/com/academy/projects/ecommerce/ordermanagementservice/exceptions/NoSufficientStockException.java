package com.academy.projects.ecommerce.ordermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class NoSufficientStockException extends RuntimeException {
    public NoSufficientStockException(String variantId, String sellerId) {
        super("No enough stock available under Variant: '" + variantId + "' and Seller: '" + sellerId + "'!!!");
    }
}
