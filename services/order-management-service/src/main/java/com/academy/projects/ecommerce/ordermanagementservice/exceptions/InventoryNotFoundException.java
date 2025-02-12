package com.academy.projects.ecommerce.ordermanagementservice.exceptions;

import com.academy.projects.ecommerce.ordermanagementservice.models.InventoryUnit;
import lombok.ToString;

@ToString
public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(InventoryUnit inventoryUnit) {
        super("Inventory with Product Id: '" + inventoryUnit.getVariantId() + "' and Seller Id: '" + inventoryUnit.getSellerId() + "' not found");
    }

    public InventoryNotFoundException(String inventoryId, String sellerId) {
        super("Inventory with Inventory Id: '" + inventoryId + "' and Seller Id: '" + sellerId + "' not found!!!");
    }

    public InventoryNotFoundException(String inventoryId) {
        super("Inventory with Inventory Id: '" + inventoryId + "' not found!!!");
    }

    public InventoryNotFoundException(String variantId, String sellerId, String message) {
        super("Inventory with Variant Id: '" + variantId + "' and Seller Id: '" + sellerId + "' not found!!! " + message);
    }
}
