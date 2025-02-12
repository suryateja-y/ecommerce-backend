package com.academy.projects.ecommerce.inventorymanagementservice.kafka.producer.inventory;

import com.academy.projects.ecommerce.inventorymanagementservice.kafka.dtos.InventoryUnitDto;
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

}
