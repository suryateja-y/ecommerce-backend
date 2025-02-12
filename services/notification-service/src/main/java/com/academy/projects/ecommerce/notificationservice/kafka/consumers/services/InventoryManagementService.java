package com.academy.projects.ecommerce.notificationservice.kafka.consumers.services;

import com.academy.projects.ecommerce.notificationservice.exceptions.InvalidActionException;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.ApprovalStatus;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.InventoryDto;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.InventoryUnit;
import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.User;
import com.academy.projects.ecommerce.notificationservice.services.notifications.INotificationManagementService;
import com.academy.projects.ecommerce.notificationservice.services.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryManagementService {
    private final IUserService userService;
    private final INotificationManagementService notificationManagementService;
    private final Logger logger = LoggerFactory.getLogger(InventoryManagementService.class);

    @Autowired
    public InventoryManagementService(IUserService userService, INotificationManagementService notificationManagementService) {
        this.userService = userService;
        this.notificationManagementService = notificationManagementService;
    }

    @KafkaListener(topics = "${application.kafka.topics.inventory-topic}", groupId = "${application.kafka.consumer.inventory-group}", containerFactory = "kafkaListenerContainerFactoryForInventory")
    public void consumeInventory(InventoryDto inventoryDto) {
        try {
            User seller = userService.getByUserid(inventoryDto.getInventoryUnit().getSellerId());
            switch (inventoryDto.getAction()) {
                case CREATE, UPDATE:
                    sendCreateUpdate(inventoryDto, seller);
                    break;
                case STATUS_UPDATE:
                    sendStatus(inventoryDto, seller);
                    break;
                default:
                    throw new InvalidActionException(inventoryDto.getAction());
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.inventory-update-topic}", groupId = "${application.kafka.consumer.inventory-update-group}", containerFactory = "kafkaListenerContainerFactoryForInventory")
    public void consumeInventoryUpdate(InventoryDto inventoryDto) {
        try {
            if(!inventoryDto.getAction().equals(Action.DELETE)) return;
            User seller = userService.getByUserid(inventoryDto.getInventoryUnit().getSellerId());
            String registryKey = "no-stock";
            RecipientCommunicationDetails communicationDetails = getCommunicationDetails(seller.getEmail());
            notificationManagementService.send(registryKey, prepareData(inventoryDto.getInventoryUnit(), seller), communicationDetails);
            logger.info("Sent no stock notification to '{}'", seller.getEmail());
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void sendCreateUpdate(InventoryDto inventoryDto, User seller) {
        String registryKey = getRegistryKey(inventoryDto.getAction());
        RecipientCommunicationDetails communicationDetails = getCommunicationDetails(seller.getEmail());
        notificationManagementService.send(registryKey, prepareData(inventoryDto.getInventoryUnit(), seller), communicationDetails);
        logger.info("Sent inventory notification to '{}'", seller.getEmail());
    }

    private void sendStatus(InventoryDto inventoryDto, User seller) {
        InventoryUnit inventoryUnit = inventoryDto.getInventoryUnit();
        if(inventoryUnit.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
            sendApproved(inventoryDto, seller);
        } else {
            sendCreateUpdate(inventoryDto, seller);
        }
    }

    public void sendApproved(InventoryDto inventoryDto, User seller) {
        Map<String, Object> data = prepareData(inventoryDto.getInventoryUnit(), seller);
        RecipientCommunicationDetails communicationDetails = getCommunicationDetails(seller.getEmail());
        notificationManagementService.send("inventory-approved", data, communicationDetails);
        logger.info("Sent inventory approved notification to '{}'", seller.getEmail());
    }

    private String getRegistryKey(Action action) {
        return switch (action) {
            case CREATE -> "inventory-create";
            case UPDATE -> "inventory-update";
            case STATUS_UPDATE -> "inventory-status";
            default -> throw new InvalidActionException(action);
        };
    }

    private RecipientCommunicationDetails getCommunicationDetails(String email) {
        return RecipientCommunicationDetails.builder()
                .email(email)
                .build();
    }

    private Map<String, Object> prepareData(InventoryUnit inventoryUnit, User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("userName", user.getFullName());
        data.put("inventoryId", inventoryUnit.getId());
        data.put("variantId", inventoryUnit.getVariantId());
        data.put("quantity", inventoryUnit.getQuantity());
        data.put("unitPrice", inventoryUnit.getUnitPrice());
        data.put("approvalId", inventoryUnit.getApprovalId());
        return data;
    }

}
