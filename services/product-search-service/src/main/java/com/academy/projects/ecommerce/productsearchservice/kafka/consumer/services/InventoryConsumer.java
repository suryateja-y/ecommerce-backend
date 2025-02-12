package com.academy.projects.ecommerce.productsearchservice.kafka.consumer.services;

import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.InventoryUnitDto;
import com.academy.projects.ecommerce.productsearchservice.services.IInventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumer {

    private final IInventoryService inventoryService;

    public InventoryConsumer(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    @KafkaListener(topics = "${application.kafka.topics.inventory-update-topic}", groupId = "${application.kafka.consumer.inventory-update-group}", containerFactory = "kafkaListenerContainerFactoryForInventory")
    public void consumer(InventoryUnitDto inventoryUnitDto) {
        try {
            inventoryService.add(inventoryUnitDto);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
