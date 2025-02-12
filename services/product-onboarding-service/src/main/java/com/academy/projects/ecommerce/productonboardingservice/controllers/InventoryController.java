package com.academy.projects.ecommerce.productonboardingservice.controllers;

import com.academy.projects.ecommerce.productonboardingservice.dtos.InventoryRequestDto;
import com.academy.projects.ecommerce.productonboardingservice.dtos.InventoryResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.InventoryNotFoundException;
import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.InventoryUnit;
import com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement.IInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/product-onboardings/inventory")
public class InventoryController {
    private final IInventoryService inventoryService;

    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    public InventoryController(final IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public ResponseEntity<InventoryResponseDto> addInventory(@RequestBody InventoryRequestDto requestDto, Authentication authentication) {
        InventoryResponseDto responseDto = new InventoryResponseDto();
        try {
            InventoryUnit inventoryUnit = inventoryService.add(requestDto, authentication.getName());
            responseDto.setInventoryUnit(inventoryUnit);
            responseDto.setMessage("Successfully added the inventory!!!");
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch(Exception e) {
            responseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public ResponseEntity<InventoryResponseDto> updateInventory(@RequestBody InventoryUnit inventoryUnit, Authentication authentication) {
        InventoryResponseDto responseDto = new InventoryResponseDto();
        try {
            InventoryUnit savedInventoryUnit = inventoryService.update(inventoryUnit, authentication.getName());
            responseDto.setInventoryUnit(savedInventoryUnit);
            responseDto.setMessage("Successfully updated the inventory!!!");
            return new ResponseEntity<>(responseDto, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public ResponseEntity<List<InventoryUnit>> getInventory(Authentication authentication, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) ApprovalStatus approvalStatus) {
        List<InventoryUnit> inventoryUnits = inventoryService.getInventory(authentication.getName(), page, pageSize, approvalStatus);
        return new ResponseEntity<>(inventoryUnits, HttpStatus.OK);
    }

    @GetMapping("/{inventoryId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public ResponseEntity<InventoryUnit> getInventoryById(@PathVariable String inventoryId, Authentication authentication) {
        InventoryUnit inventoryUnit = inventoryService.getInventory(inventoryId, authentication.getName());
        return new ResponseEntity<>(inventoryUnit, HttpStatus.OK);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ROLE_SELLER_MANAGER', 'ROLE_INVENTORY_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<InventoryUnit>> getInventory(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) ApprovalStatus approvalStatus) {
        List<InventoryUnit> inventoryUnits = inventoryService.getInventory(page, pageSize, approvalStatus);
        return new ResponseEntity<>(inventoryUnits, HttpStatus.OK);
    }

    @GetMapping("/admin/{inventoryId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER_MANAGER', 'ROLE_INVENTORY_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<InventoryUnit> getInventoryById(@PathVariable String inventoryId) {
        InventoryUnit inventoryUnit = inventoryService.getInventory(inventoryId);
        return new ResponseEntity<>(inventoryUnit, HttpStatus.OK);
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<String> handleInventoryNotFoundException(InventoryNotFoundException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
