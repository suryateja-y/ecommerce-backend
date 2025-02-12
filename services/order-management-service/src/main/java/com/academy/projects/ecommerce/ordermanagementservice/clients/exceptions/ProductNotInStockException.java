package com.academy.projects.ecommerce.ordermanagementservice.clients.exceptions;

import lombok.ToString;

@ToString
public class ProductNotInStockException extends RuntimeException {
    public ProductNotInStockException(String variantId, String sellerId) {
        super("Product with Variant Id: '" + variantId + "' and Seller Id: '" + sellerId + "' is unavailable!!!");
    }
}
