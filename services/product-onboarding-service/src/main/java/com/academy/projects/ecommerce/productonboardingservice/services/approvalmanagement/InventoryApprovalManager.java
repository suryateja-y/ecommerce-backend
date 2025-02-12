package com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement;

import com.academy.projects.ecommerce.productonboardingservice.starters.GlobalData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.academy.projects.ecommerce.productonboardingservice.clients.dtos.ApprovalResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.clients.dtos.ResponseStatus;
import com.academy.projects.ecommerce.productonboardingservice.clients.services.ApprovalManagementServiceClient;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.InventoryApprovalRegistrationException;
import com.academy.projects.ecommerce.productonboardingservice.kafka.producer.services.InventoryProducer;
import com.academy.projects.ecommerce.productonboardingservice.models.InventoryUnit;
import com.academy.projects.ecommerce.productonboardingservice.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryApprovalManager implements IInventoryApprovalManager {
    @Value("${application.kafka.topics.inventory-approval-topic}")
    private String inventoryApprovalTopic;

    private final ApprovalManagementServiceClient approvalManagementServiceClient;
    private final ObjectMapper objectMapper;
    private final InventoryRepository inventoryRepository;
    private final InventoryProducer inventoryProducer;

    @Autowired
    public InventoryApprovalManager(final ApprovalManagementServiceClient approvalManagementServiceClient, final ObjectMapper objectMapper, final InventoryRepository inventoryRepository, final InventoryProducer inventoryProducer) {
        this.approvalManagementServiceClient = approvalManagementServiceClient;
        this.objectMapper = objectMapper;
        this.inventoryRepository = inventoryRepository;
        this.inventoryProducer = inventoryProducer;
    }

    @Override
    public String register(InventoryUnit inventoryUnit, ActionType actionType) {
        try {
            ApprovalRequest approvalRequest = getApprovalRequest(inventoryUnit);
            ApprovalResponseDto responseDto = approvalManagementServiceClient.registerForApproval(approvalRequest);
            if(responseDto.getResponseStatus() == ResponseStatus.SUCCESS) {
                inventoryUnit.setApprovalId(responseDto.getApprovalId());

                inventoryProducer.send(inventoryUnit, actionType);

                return responseDto.getApprovalId();
            } else throw new InventoryApprovalRegistrationException(inventoryUnit.getId());
        } catch(Exception e) {
            throw new InventoryApprovalRegistrationException(inventoryUnit.getId());
        }
    }

    @Override
    public void updateStatus(ApprovalRequest approvalRequest) {
        InventoryUnit inventoryUnit = this.getInventory(approvalRequest);
        InventoryUnit savedInventory = inventoryRepository.findById(inventoryUnit.getId()).orElse(null);
        if(savedInventory != null) {
            savedInventory.setApprovalStatus(approvalRequest.getStatus());
            savedInventory = inventoryRepository.save(savedInventory);

            // Send Inventory to Product Service using Kafka
            inventoryProducer.send(savedInventory, ActionType.STATUS_UPDATE);
        }
    }

    private InventoryUnit getInventory(ApprovalRequest approvalRequest) {
        return objectMapper.convertValue(approvalRequest.getData(), InventoryUnit.class);
    }

    private ApprovalRequest getApprovalRequest(InventoryUnit inventoryUnit) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setRequester(SecurityContextHolder.getContext().getAuthentication().getName());
        approvalRequest.setTopic(inventoryApprovalTopic);
        approvalRequest.setTitle("Inventory Approval");
        approvalRequest.setData(inventoryUnit);
        approvalRequest.setActionType(ActionType.CREATE);
        approvalRequest.setApprovers(List.of(GlobalData.INVENTORY_MANAGER_ID, GlobalData.PRODUCT_MANAGER_ID, GlobalData.ADMIN_ID));
        return approvalRequest;
    }
}
