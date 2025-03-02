package com.academy.projects.ecommerce.productsearchservice.controllers;

import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryFeasibilityDetails;
import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryFeasibilityRequestDto;
import com.academy.projects.ecommerce.productsearchservice.services.IDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/delivery-details")
public class DetailsController {

    private final IDetailsService detailsService;

    @Autowired
    public DetailsController(final IDetailsService detailsService) {
        this.detailsService = detailsService;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
    public ResponseEntity<List<DeliveryFeasibilityDetails>> getDeliveryFeasibilityDetails(@RequestBody DeliveryFeasibilityRequestDto requestDto) {
        List<DeliveryFeasibilityDetails> feasibilityDetails = detailsService.get(requestDto.getItems(), requestDto.getCustomerAddress());
        return ResponseEntity.ok(feasibilityDetails);
    }
}
