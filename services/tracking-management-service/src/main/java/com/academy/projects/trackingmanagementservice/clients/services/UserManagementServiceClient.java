package com.academy.projects.trackingmanagementservice.clients.services;

import com.academy.projects.trackingmanagementservice.models.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${application.services.user-management-service}")
public interface UserManagementServiceClient {
    @GetMapping("/api/${application.version}/users/customers/{customerId}/addresses/{addressId}")
    ResponseEntity<Address> getCustomerAddress(@PathVariable String customerId, @PathVariable String addressId);

    @GetMapping("/api/${application.version}/users/sellers/{sellerId}/addresses")
    ResponseEntity<Address> getSellerAddress(@PathVariable String sellerId);
}
