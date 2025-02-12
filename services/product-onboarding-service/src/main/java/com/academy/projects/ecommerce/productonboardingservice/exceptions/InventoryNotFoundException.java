package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(String inventoryId) {
        super("Inventory with id '" + inventoryId + "' not found!!!");
    }

    public InventoryNotFoundException(String inventoryId, String sellerId) {
        super("Inventory with id '" + inventoryId + "' and seller '" + sellerId + "' not found!!!");
    }
}
