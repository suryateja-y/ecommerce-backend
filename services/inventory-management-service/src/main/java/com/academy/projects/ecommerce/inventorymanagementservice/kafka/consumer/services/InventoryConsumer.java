package com.academy.projects.ecommerce.inventorymanagementservice.kafka.consumer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.academy.projects.ecommerce.inventorymanagementservice.kafka.dtos.ActionType;
import com.academy.projects.ecommerce.inventorymanagementservice.kafka.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.inventorymanagementservice.kafka.dtos.ApprovalStatus;
import com.academy.projects.ecommerce.inventorymanagementservice.models.InventoryUnit;
import com.academy.projects.ecommerce.inventorymanagementservice.services.IInventoryService;
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

    @KafkaListener(topics = "product-approval", groupId = "product-approval-group-3")
    public void consumeProductApproval(ApprovalRequest approvalRequest) {
        try {
            if((approvalRequest != null) && approvalRequest.getActionType().equals(ActionType.DELETE)) {
                InventoryUnit inventoryUnit = objectMapper.convertValue(approvalRequest.getData(), InventoryUnit.class);
                inventoryService.delete(inventoryUnit);
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }
}
