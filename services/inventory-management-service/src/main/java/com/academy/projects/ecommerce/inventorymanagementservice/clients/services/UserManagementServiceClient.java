package com.academy.projects.ecommerce.inventorymanagementservice.clients.services;

import com.academy.projects.ecommerce.inventorymanagementservice.models.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${application.services.user-management-service}")
public interface UserManagementServiceClient {
    @GetMapping("/api/${application.version}/users/sellers/{sellerId}/addresses")
    Address getAddress(@PathVariable String sellerId);
}
