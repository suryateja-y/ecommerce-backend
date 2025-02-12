package com.academy.projects.ecommerce.ordermanagementservice.clients.services;

import com.academy.projects.ecommerce.ordermanagementservice.clients.dtos.Cart;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${application.services.cart-management-service}")
public interface CartServiceClient {
    @GetMapping("/api/${application.version}/carts")
    ResponseEntity<Cart> getCart();
}
