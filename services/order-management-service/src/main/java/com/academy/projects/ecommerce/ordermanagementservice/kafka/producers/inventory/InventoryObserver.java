package com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.inventory;

import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.InventoryUnitDto;

public interface InventoryObserver {
    void sendUpdate(InventoryUnitDto inventoryUnitDto);
}
