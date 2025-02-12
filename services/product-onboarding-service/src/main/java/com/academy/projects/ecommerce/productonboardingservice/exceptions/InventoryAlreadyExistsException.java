package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class InventoryAlreadyExistsException extends RuntimeException {
    public InventoryAlreadyExistsException(String productId, String sellerId) {
        super("Inventory already exists for Product Id '" + productId + "' and Seller Id '" + sellerId + "'!!!");
    }
}
