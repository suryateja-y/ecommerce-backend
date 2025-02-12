package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class InventoryApprovalRegistrationException extends RuntimeException {
    public InventoryApprovalRegistrationException(String inventoryId) {
        super("Failed to register the inventory: '" + inventoryId + "' for the approval!!!");
    }
}
