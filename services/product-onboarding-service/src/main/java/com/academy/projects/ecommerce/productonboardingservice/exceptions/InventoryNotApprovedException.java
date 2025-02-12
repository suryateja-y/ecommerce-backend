package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class InventoryNotApprovedException extends RuntimeException {
    public InventoryNotApprovedException(String inventoryId) {
        super("The inventory " + inventoryId + " is not approved!!!");
    }
}
