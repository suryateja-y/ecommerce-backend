package com.academy.projects.ecommerce.ordermanagementservice.controllers;

import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrder;
import com.academy.projects.ecommerce.ordermanagementservice.models.PreOrderStatus;
import com.academy.projects.ecommerce.ordermanagementservice.services.IPreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/preorders")
public class PreOrderController {
    private final IPreOrderService preOrderService;

    @Autowired
    public PreOrderController(final IPreOrderService preOrderService) {
        this.preOrderService = preOrderService;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<PreOrder> checkout(@RequestBody String addressId, Authentication authentication) {
        PreOrder preOrder = preOrderService.checkout(authentication.getName(), addressId);
        return new ResponseEntity<>(preOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('ROLE_ORDER_MANAGER', 'ROLE_ADMIN') or ( hasRole('ROLE_CUSTOMER') and @accessChecker.isOwner(#customerId) )")
    public ResponseEntity<List<PreOrder>> getOrders(@PathVariable String customerId, @RequestParam(required = false) PreOrderStatus preOrderStatus, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<PreOrder> preOrders = preOrderService.getOrders(customerId, preOrderStatus, page, pageSize);
        return new ResponseEntity<>(preOrders, HttpStatus.OK);
    }
}
