package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.InventoryDetailsRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.InventoryNotFoundException;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.NoSufficientStockException;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.inventory.InventoryUpdateManager;
import com.academy.projects.ecommerce.ordermanagementservice.models.InventoryUnit;
import com.academy.projects.ecommerce.ordermanagementservice.models.OrderItem;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryUpdateManager inventoryUpdateManager;

    private final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, InventoryUpdateManager inventoryUpdateManager) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryUpdateManager = inventoryUpdateManager;
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

        // Sending to Kafka
        inventoryUpdateManager.send(inventoryUnit, Action.CREATE);

        logger.info("Inventory {} has been created!!!", savedInventoryUnit);
        return savedInventoryUnit;
    }

    @Override
    public void delete(InventoryUnit inventoryUnit) {
        InventoryUnit savedInventoryUnit = this.getInventoryUnit(inventoryUnit);
        if(savedInventoryUnit != null) {
            savedInventoryUnit.setQuantity(0L);
            inventoryRepository.save(savedInventoryUnit);
            inventoryUpdateManager.send(inventoryUnit, Action.DELETE);
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

    @Override
    public void block(Set<OrderItem> orderItems) {
        List<InventoryUnit> inventoryUnits = new LinkedList<>();
        List<InventoryUnit> zeroStockInventoryUnits = new LinkedList<>();
        synchronized (this) {
            for (OrderItem orderItem : orderItems) {
                InventoryUnit inventoryUnit = inventoryRepository.findByVariantIdAndSellerId(orderItem.getVariantId(), orderItem.getSellerId()).orElseThrow(() -> new InventoryNotFoundException(orderItem.getVariantId(), orderItem.getSellerId()));
                if (inventoryUnit.getQuantity() < orderItem.getQuantity())
                    throw new NoSufficientStockException(inventoryUnit.getVariantId(), inventoryUnit.getSellerId());
                inventoryUnit.setQuantity(inventoryUnit.getQuantity() - orderItem.getQuantity());
                inventoryUnits.add(inventoryUnit);
                if (inventoryUnit.getQuantity() == 0) zeroStockInventoryUnits.add(inventoryUnit);
            }
            inventoryRepository.saveAll(inventoryUnits);
        }
        // Send to Kafka to send a mail to the seller
        for(InventoryUnit inventoryUnit : zeroStockInventoryUnits)
            inventoryUpdateManager.send(inventoryUnit, Action.DELETE);
    }

    @Override
    public void release(Set<OrderItem> orderItems) {
        List<InventoryUnit> inventoryUnits = new LinkedList<>();
        synchronized (this) {
            for (OrderItem orderItem : orderItems) {
                InventoryUnit inventoryUnit = inventoryRepository.findByVariantIdAndSellerId(orderItem.getVariantId(), orderItem.getSellerId()).orElseThrow(() -> new InventoryNotFoundException(orderItem.getVariantId(), orderItem.getSellerId()));
                inventoryUnit.setQuantity(inventoryUnit.getQuantity() + orderItem.getQuantity());
                inventoryUnits.add(inventoryUnit);
            }
            inventoryRepository.saveAll(inventoryUnits);
        }
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
