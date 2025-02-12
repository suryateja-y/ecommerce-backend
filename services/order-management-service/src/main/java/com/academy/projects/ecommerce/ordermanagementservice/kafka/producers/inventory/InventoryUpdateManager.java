package com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.inventory;

import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.InventoryUnitDto;
import com.academy.projects.ecommerce.ordermanagementservice.models.InventoryUnit;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class InventoryUpdateManager {
    private final List<InventoryObserver> observers = new LinkedList<>();

    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }
    public void notifyObservers(InventoryUnitDto inventoryUnitDto) {
        for (InventoryObserver observer : observers) {
            observer.sendUpdate(inventoryUnitDto);
        }
    }

    public void send(InventoryUnit inventoryUnit, Action action) {
        InventoryUnitDto inventoryUnitDto = InventoryUnitDto.builder().inventoryUnit(inventoryUnit).action(action).build();
        notifyObservers(inventoryUnitDto);
    }
}
