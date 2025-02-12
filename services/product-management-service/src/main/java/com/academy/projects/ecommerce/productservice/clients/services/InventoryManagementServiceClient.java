package com.academy.projects.ecommerce.productservice.clients.services;

import com.academy.projects.ecommerce.productservice.clients.dtos.DetailsRequestDto;
import com.academy.projects.ecommerce.productservice.clients.dtos.DetailsResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${application.services.inventory-management-service}")
public interface InventoryManagementServiceClient {
    @PostMapping("/api/${application.version}/inventory/details")
    ResponseEntity<DetailsResponseDto> calculate(@RequestBody DetailsRequestDto requestDto);
}
