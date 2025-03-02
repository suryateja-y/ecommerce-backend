package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.InventoryUnitDto;
import com.academy.projects.ecommerce.productsearchservice.models.InventoryUnit;

import java.util.List;

public interface IInventoryService {
    InventoryUnit add(InventoryUnitDto inventoryUnitDto);
    List<InventoryUnit> getAll(String productId, String variantId);
    InventoryUnit get(String variantId, String sellerId);
}
