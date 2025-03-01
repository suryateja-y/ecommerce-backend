package com.academy.projects.ecommerce.ordermanagementservice.starters;

import com.academy.projects.ecommerce.ordermanagementservice.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InitializeData(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(!alreadySetup) return;
        inventoryRepository.deleteAll();
        alreadySetup = true;
    }
}
