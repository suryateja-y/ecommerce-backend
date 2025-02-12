package com.academy.projects.ecommerce.productonboardingservice.kafka.producer.services;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.kafka.dtos.InventoryDto;
import com.academy.projects.ecommerce.productonboardingservice.models.InventoryUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryProducer {

    @Value("${application.kafka.topics.inventory-topic}")
    private String inventoryTopic;

    private final KafkaTemplate<String, InventoryDto> inventoryKafkaTemplate;

    @Autowired
    public InventoryProducer(final KafkaTemplate<String, InventoryDto> inventoryKafkaTemplate) {
        this.inventoryKafkaTemplate = inventoryKafkaTemplate;
    }

    public void send(InventoryUnit inventoryUnit, ActionType actionType) {
        InventoryDto inventoryDto = InventoryDto.builder().inventoryUnit(inventoryUnit).action(actionType).build();
        inventoryKafkaTemplate.send(inventoryTopic, inventoryDto);
    }
}
