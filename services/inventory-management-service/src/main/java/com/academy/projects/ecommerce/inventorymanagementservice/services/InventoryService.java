package com.academy.projects.ecommerce.inventorymanagementservice.services;

import com.academy.projects.ecommerce.inventorymanagementservice.dtos.InventoryDetailsRequestDto;
import com.academy.projects.ecommerce.inventorymanagementservice.exceptions.InventoryNotFoundException;
import com.academy.projects.ecommerce.inventorymanagementservice.models.InventoryUnit;
import com.academy.projects.ecommerce.inventorymanagementservice.repositories.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class
InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;

    private final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryUnit add(InventoryUnit inventoryUnit) {
        InventoryUnit savedInventoryUnit = this.getInventoryUnit(inventoryUnit);
        if(savedInventoryUnit != null) {
            savedInventoryUnit.setQuantity(inventoryUnit.getQuantity());
            savedInventoryUnit.setUnitPrice(inventoryUnit.getUnitPrice());
            savedInventoryUnit = inventoryRepository.save(savedInventoryUnit);
        } else {
            savedInventoryUnit = inventoryRepository.save(inventoryUnit);
        }
        logger.info("Inventory {} has been created!!!", savedInventoryUnit);
        return savedInventoryUnit;
    }

    @Override
    public void delete(InventoryUnit inventoryUnit) {
        InventoryUnit savedInventoryUnit = this.getInventoryUnit(inventoryUnit);
        if(savedInventoryUnit != null) {
            savedInventoryUnit.setQuantity(0L);
            inventoryRepository.save(savedInventoryUnit);
        } else throw new InventoryNotFoundException(inventoryUnit);
    }

    @Override
    public List<InventoryUnit> getInventory(String sellerId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return inventoryRepository.findAllBySellerId(sellerId, pageable).getContent();
    }

    @Override
    public InventoryUnit getInventory(String inventoryId, String sellerId) {
        return inventoryRepository.findByIdAndSellerId(inventoryId, sellerId).orElseThrow(() -> new InventoryNotFoundException(inventoryId, sellerId));
    }

    @Override
    public List<InventoryUnit> getInventory(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return inventoryRepository.findAll(pageable).getContent();
    }

    @Override
    public InventoryUnit getInventory(String inventoryId) {
        return inventoryRepository.findById(inventoryId).orElseThrow(() -> new InventoryNotFoundException(inventoryId));
    }

    @Override
    public InventoryUnit getInventoryByVariantIdAndSellerId(String variantId, String sellerId) {
        return inventoryRepository.findByVariantIdAndSellerId(variantId, sellerId).orElseThrow(() -> new InventoryNotFoundException(variantId, sellerId, ""));
    }

    @Override
    public List<InventoryUnit> getAllByVariant(String variantId) {
        return inventoryRepository.findAllByVariantIdAndQuantityGreaterThan(variantId, 0L);
    }

    @Override
    public List<InventoryUnit> getInventory(List<InventoryDetailsRequestDto> requests) {
        List<InventoryUnit> inventoryUnits = new LinkedList<>();
        for(InventoryDetailsRequestDto request : requests)
            inventoryUnits.add(getInventoryByVariantIdAndSellerId(request.getVariantId(), request.getSellerId()));
        return inventoryUnits;
    }

    private InventoryUnit getInventoryUnit(InventoryUnit inventoryUnit) {
        InventoryUnit savedInventoryUnit = null;
        if(inventoryUnit.getId() != null)
            savedInventoryUnit = inventoryRepository.findById(inventoryUnit.getId()).orElse(null);
        if (savedInventoryUnit == null)
            savedInventoryUnit = inventoryRepository.findByVariantIdAndSellerId(inventoryUnit.getVariantId(), inventoryUnit.getSellerId()).orElse(null);
        return savedInventoryUnit;
    }
}
