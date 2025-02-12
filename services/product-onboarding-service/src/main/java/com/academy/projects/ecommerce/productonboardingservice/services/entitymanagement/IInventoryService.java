package com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement;

import com.academy.projects.ecommerce.productonboardingservice.dtos.InventoryRequestDto;
import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.InventoryUnit;

import java.util.List;

public interface IInventoryService {
    InventoryUnit add(InventoryRequestDto requestDto, String sellerId);
    List<InventoryUnit> getInventory(String sellerId, int page, int pageSize, ApprovalStatus approvalStatus);
    List<InventoryUnit> getInventory(int page, int pageSize, ApprovalStatus approvalStatus);
    InventoryUnit getInventory(String sellerId, String inventoryId);
    InventoryUnit getInventory(String inventoryId);
    InventoryUnit update(InventoryUnit inventoryUnit, String sellerId);
}
