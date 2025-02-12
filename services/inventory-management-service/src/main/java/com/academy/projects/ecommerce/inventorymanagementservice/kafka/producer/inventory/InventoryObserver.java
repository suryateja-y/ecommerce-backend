package com.academy.projects.ecommerce.inventorymanagementservice.kafka.producer.inventory;

import com.academy.projects.ecommerce.inventorymanagementservice.kafka.dtos.InventoryUnitDto;

public interface InventoryObserver {
    void sendUpdate(InventoryUnitDto inventoryUnitDto);
}
