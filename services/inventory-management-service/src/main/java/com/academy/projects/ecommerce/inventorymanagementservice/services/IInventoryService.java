package com.academy.projects.ecommerce.inventorymanagementservice.services;

import com.academy.projects.ecommerce.inventorymanagementservice.dtos.InventoryDetailsRequestDto;
import com.academy.projects.ecommerce.inventorymanagementservice.models.InventoryUnit;

import java.util.List;

public interface IInventoryService {
    InventoryUnit add(InventoryUnit inventoryUnit);
    void delete(InventoryUnit inventoryUnit);
    List<InventoryUnit> getInventory(String sellerId, int page, int pageSize);
    InventoryUnit getInventory(String inventoryId, String sellerId);
    List<InventoryUnit> getInventory(int page, int pageSize);
    InventoryUnit getInventory(String inventoryId);
    InventoryUnit getInventoryByVariantIdAndSellerId(String variantId, String sellerId);
    List<InventoryUnit> getAllByVariant(String variantId);
    List<InventoryUnit> getInventory(List<InventoryDetailsRequestDto> requests);
}
