package com.academy.projects.ecommerce.inventorymanagementservice.configurations;

import com.academy.projects.ecommerce.inventorymanagementservice.kafka.producer.inventory.InventoryUpdateManager;
import com.academy.projects.ecommerce.inventorymanagementservice.kafka.producer.observers.ProductSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverRegistration {

    private final InventoryUpdateManager inventoryUpdateManager;
    private final ProductSearchService productSearchService;

    @Autowired
    public ObserverRegistration(InventoryUpdateManager inventoryUpdateManager, ProductSearchService productSearchService) {
        this.inventoryUpdateManager = inventoryUpdateManager;
        this.productSearchService = productSearchService;
    }

    @Bean
    public boolean registerInventoryObservers() {
        inventoryUpdateManager.addObserver(productSearchService);
        return true;
    }
}
