package com.academy.projects.ecommerce.cartmanagementservice.controllers;

import com.academy.projects.ecommerce.cartmanagementservice.models.Cart;
import com.academy.projects.ecommerce.cartmanagementservice.models.CartUnit;
import com.academy.projects.ecommerce.cartmanagementservice.services.ICartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/${application.version}/carts")
public class CartController {
    private final ICartService cartService;

    private final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    public CartController(final ICartService cartService) {
        this.cartService = cartService;
    }
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Cart> createCart(@RequestBody(required = false) Set<CartUnit> cartUnits, Authentication authentication) {
        Cart cart = cartService.add(authentication.getName(), cartUnits);
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        Cart cart = cartService.get(authentication.getName());
        return ResponseEntity.ok(cart);
    }

    @PutMapping("")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<Cart> addToCart(@RequestBody CartUnit cartUnit, Authentication authentication) {
        Cart cart = cartService.addToCart(authentication.getName(), cartUnit);
        return new ResponseEntity<>(cart, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CART_MANAGER')")
    public ResponseEntity<Cart> getCart(@PathVariable String userId) {
        Cart cart = cartService.get(userId);
        return ResponseEntity.ok(cart);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
