package com.academy.projects.ecommerce.ordermanagementservice.controllers;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.BulkInventoryDetailsRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.InventoryDetails;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.InventoryNotFoundException;
import com.academy.projects.ecommerce.ordermanagementservice.models.InventoryUnit;
import com.academy.projects.ecommerce.ordermanagementservice.services.IInventoryService;
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
@RequestMapping("/api/${application.version}/inventory")
public class InventoryController {
    private final IInventoryService inventoryService;

    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    public InventoryController(final IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public ResponseEntity<List<InventoryUnit>> getInventory(Authentication authentication, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<InventoryUnit> inventoryUnits = inventoryService.getInventory(authentication.getName(), page, pageSize);
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
    public ResponseEntity<List<InventoryUnit>> getInventory(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<InventoryUnit> inventoryUnits = inventoryService.getInventory(page, pageSize);
        return new ResponseEntity<>(inventoryUnits, HttpStatus.OK);
    }

    @GetMapping("/admin/{inventoryId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER_MANAGER', 'ROLE_INVENTORY_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<InventoryUnit> getInventoryById(@PathVariable String inventoryId) {
        InventoryUnit inventoryUnit = inventoryService.getInventory(inventoryId);
        return new ResponseEntity<>(inventoryUnit, HttpStatus.OK);
    }

    @PostMapping("/bulk-details")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<InventoryDetails>> getDetails(@RequestBody BulkInventoryDetailsRequestDto requestDto) {
        List<InventoryUnit> inventoryUnits = inventoryService.getInventory(requestDto.getRequests());
        return new ResponseEntity<>(inventoryUnits.stream().map(this::from).toList(), HttpStatus.OK);
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<String> handleInventoryNotFoundException(InventoryNotFoundException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    private InventoryDetails from(InventoryUnit inventoryUnit) {
        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setVariantId(inventoryUnit.getVariantId());
        inventoryDetails.setSellerId(inventoryUnit.getSellerId());
        inventoryDetails.setUnitPrice(inventoryUnit.getUnitPrice());
        inventoryDetails.setInStock(inventoryUnit.getQuantity() > 0);
        return inventoryDetails;
    }

}
