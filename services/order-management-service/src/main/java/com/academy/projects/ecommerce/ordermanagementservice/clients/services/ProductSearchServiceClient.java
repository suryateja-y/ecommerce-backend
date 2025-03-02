package com.academy.projects.ecommerce.ordermanagementservice.clients.services;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.DeliveryFeasibilityDetails;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.DeliveryFeasibilityRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "${application.services.product-search-service}")
public interface ProductSearchServiceClient {
    @GetMapping("/api/${application.version}/delivery-details")
    ResponseEntity<List<DeliveryFeasibilityDetails>> getDeliveryFeasibilityDetails(@RequestBody DeliveryFeasibilityRequestDto requestDto);
}
