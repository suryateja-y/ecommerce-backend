package com.academy.projects.ecommerce.cartmanagementservice.clients.services;

import com.academy.projects.ecommerce.cartmanagementservice.clients.dtos.BulkInventoryDetailsRequestDto;
import com.academy.projects.ecommerce.cartmanagementservice.dtos.InventoryDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "${application.services.inventory-management-service}")
public interface InventoryManagementServiceClient {
    @PostMapping("/api/${application.version}/inventory/bulk-details")
    ResponseEntity<List<InventoryDetails>> getDetails(@RequestBody BulkInventoryDetailsRequestDto requestDto);
}
