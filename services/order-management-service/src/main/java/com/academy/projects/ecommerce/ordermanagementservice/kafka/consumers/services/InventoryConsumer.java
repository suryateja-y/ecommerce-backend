package com.academy.projects.ecommerce.ordermanagementservice.kafka.consumers.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.ApprovalStatus;
import com.academy.projects.ecommerce.ordermanagementservice.models.InventoryUnit;
import com.academy.projects.ecommerce.ordermanagementservice.services.IInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumer {
    private final IInventoryService inventoryService;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(InventoryConsumer.class);

    @Autowired
    public InventoryConsumer(IInventoryService inventoryService, ObjectMapper objectMapper) {
        this.inventoryService = inventoryService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${application.kafka.topics.inventory-approval-topic}", groupId = "${application.kafka.consumer.inventory-approval-group}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeInventory(ApprovalRequest approvalRequest) {
        try {
            if(approvalRequest != null) {
                InventoryUnit inventoryUnit = objectMapper.convertValue(approvalRequest.getData(), InventoryUnit.class);
                if(approvalRequest.getStatus().equals(ApprovalStatus.APPROVED))
                    inventoryService.add(inventoryUnit);
                else
                    inventoryService.delete(inventoryUnit);
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

    }
}
