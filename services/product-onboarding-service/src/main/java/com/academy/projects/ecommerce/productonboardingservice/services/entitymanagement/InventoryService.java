package com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement;

import com.academy.projects.ecommerce.productonboardingservice.clients.dtos.UserType;
import com.academy.projects.ecommerce.productonboardingservice.clients.dtos.UserValidityCheckResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.clients.services.UserManagementServiceClient;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.dtos.InventoryRequestDto;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.*;
import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.InventoryUnit;
import com.academy.projects.ecommerce.productonboardingservice.models.Variant;
import com.academy.projects.ecommerce.productonboardingservice.repositories.InventoryRepository;
import com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement.IInventoryApprovalManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService implements IInventoryService {
    private final UserManagementServiceClient userManagementServiceClient;
    private final IInventoryApprovalManager inventoryApprovalManager;
    private final InventoryRepository inventoryRepository;
    private final IVariantService variantService;

    private final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    public InventoryService(UserManagementServiceClient userManagementServiceClient, IInventoryApprovalManager inventoryApprovalManager, InventoryRepository inventoryRepository, IVariantService variantService) {
        this.userManagementServiceClient = userManagementServiceClient;
        this.inventoryApprovalManager = inventoryApprovalManager;
        this.inventoryRepository = inventoryRepository;
        this.variantService = variantService;
    }

    @Override
    public InventoryUnit add(InventoryRequestDto requestDto, String sellerId) {
        // Check the seller is valid and approved
        UserValidityCheckResponseDto responseDto = this.userManagementServiceClient.isUserValid(UserType.SELLER);
        if(!responseDto.isValid()) throw new SellerNotValidException(responseDto.getMessage());

        // Check the Variant is valid and approved
        Variant variant = variantService.getVariant(requestDto.getVariantId());
        if(!variant.getApprovalStatus().equals(ApprovalStatus.APPROVED)) throw new VariantNotApprovedException(variant.getId());

        InventoryUnit inventoryUnit = from(requestDto, sellerId);
        if(inventoryRepository.existsByVariantIdAndSellerId(inventoryUnit.getVariantId(), sellerId)) throw new InventoryAlreadyExistsException(inventoryUnit.getVariantId(), inventoryUnit.getSellerId());

        // Create Approval Request
        String approvalRequestId = this.inventoryApprovalManager.register(inventoryUnit, ActionType.CREATE);
        inventoryUnit.setApprovalId(approvalRequestId);

        // Send notification with approval request details
        logger.info("Inventory Approval Request Id: {}", approvalRequestId);

        return inventoryRepository.save(inventoryUnit);

    }

    @Override
    public List<InventoryUnit> getInventory(String sellerId, int page, int pageSize, ApprovalStatus approvalStatus) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if(approvalStatus != null)
            return inventoryRepository.findAllBySellerIdAndApprovalStatus(sellerId, approvalStatus, pageable).getContent();
        else
            return inventoryRepository.findAllBySellerId(sellerId, pageable).getContent();
    }

    @Override
    public List<InventoryUnit> getInventory(int page, int pageSize, ApprovalStatus approvalStatus) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if(approvalStatus != null)
            return inventoryRepository.findAllByApprovalStatus(approvalStatus, pageable).getContent();
        else
            return inventoryRepository.findAll(pageable).getContent();
    }

    @Override
    public InventoryUnit getInventory(String inventoryId, String sellerId) {
        return inventoryRepository.findByIdAndSellerId(inventoryId, sellerId).orElseThrow(() -> new InventoryNotFoundException(inventoryId, sellerId));
    }

    @Override
    public InventoryUnit getInventory(String inventoryId) {
        return inventoryRepository.findById(inventoryId).orElseThrow(() -> new InventoryNotFoundException(inventoryId));
    }

    @Override
    public InventoryUnit update(InventoryUnit inventoryUnit, String sellerId) {
        // Check the seller is valid and approved
        UserValidityCheckResponseDto responseDto = this.userManagementServiceClient.isUserValid(UserType.SELLER);
        if(!responseDto.isValid()) throw new SellerNotValidException(responseDto.getMessage());

        InventoryUnit savedInventory = inventoryRepository.findByIdAndSellerId(inventoryUnit.getId(), sellerId).orElseThrow(() -> new InventoryNotFoundException(inventoryUnit.getId(), sellerId));

        if(!savedInventory.getApprovalStatus().equals(ApprovalStatus.APPROVED)) throw new InventoryNotApprovedException(savedInventory.getId());
        if(inventoryUnit.getQuantity() != null)
            savedInventory.setQuantity(inventoryUnit.getQuantity());

        if(inventoryUnit.getUnitPrice() != null)
            savedInventory.setUnitPrice(inventoryUnit.getUnitPrice());

        // Create Approval Request
        String approvalRequestId = this.inventoryApprovalManager.register(savedInventory, ActionType.UPDATE);
        savedInventory.setApprovalId(approvalRequestId);

        // Send notification with approval request details
        logger.info("Inventory Update Approval Request Id: {}", approvalRequestId);

        return inventoryRepository.save(savedInventory);
    }

    private InventoryUnit from(InventoryRequestDto requestDto, String sellerId) {
        InventoryUnit inventoryUnit = new InventoryUnit();
        inventoryUnit.setVariantId(requestDto.getVariantId());
        inventoryUnit.setQuantity(requestDto.getQuantity());
        inventoryUnit.setUnitPrice(requestDto.getUnitPrice());
        inventoryUnit.setSellerId(sellerId);
        inventoryUnit.setApprovalStatus(ApprovalStatus.PENDING_FOR_APPROVAL);
        return inventoryUnit;
    }
}
