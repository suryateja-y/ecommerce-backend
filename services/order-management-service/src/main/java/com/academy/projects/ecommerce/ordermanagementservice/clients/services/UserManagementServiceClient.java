package com.academy.projects.ecommerce.ordermanagementservice.clients.services;

import com.academy.projects.ecommerce.ordermanagementservice.models.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${application.services.user-management-service}")
public interface UserManagementServiceClient {
    @GetMapping("/api/${application.version}/users/sellers/{sellerId}/addresses")
    Address getSellerAddress(@PathVariable String sellerId);

    @GetMapping("/api/${application.version}/users/customers/addresses/{addressId}")
    ResponseEntity<Address> getCustomerAddress(@PathVariable String addressId);
}
