package com.academy.projects.ecommerce.ordermanagementservice.services;


import com.academy.projects.ecommerce.ordermanagementservice.dtos.InventoryDetailsRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.InventoryUnit;
import com.academy.projects.ecommerce.ordermanagementservice.models.OrderItem;
import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrderItem;

import java.util.List;
import java.util.Set;

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
    void blockInventory(Set<PreOrderItem> orderItems);
    void releaseInventory(Set<PreOrderItem> orderItems);
    void block(Set<OrderItem> orderItems);
    void release(Set<OrderItem> orderItems);
}
