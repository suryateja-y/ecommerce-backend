package com.academy.projects.ecommerce.inventorymanagementservice.exceptions;

import lombok.ToString;

@ToString
public class SellerNotFoundException extends RuntimeException {
    public SellerNotFoundException(String sellerId, String message) {
        super("Seller with id '" + sellerId + "' not found!!! " + message);
    }
}
