package com.academy.projects.ecommerce.inventorymanagementservice.kafka.producer.observers;

import com.academy.projects.ecommerce.inventorymanagementservice.kafka.dtos.InventoryUnitDto;
import com.academy.projects.ecommerce.inventorymanagementservice.kafka.producer.inventory.InventoryObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductSearchService implements InventoryObserver {

    private final KafkaTemplate<String, InventoryUnitDto> kafkaTemplate;

    @Value("${application.kafka.topics.inventory-update-topic}")
    private String productSearchInventoryTopic;

    @Autowired
    public ProductSearchService(KafkaTemplate<String, InventoryUnitDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public void sendUpdate(InventoryUnitDto inventoryUnitDto) {
        kafkaTemplate.send(productSearchInventoryTopic, inventoryUnitDto);
    }
}
