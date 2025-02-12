package com.academy.projects.ecommerce.ordermanagementservice.controllers;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.DetailsRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.DetailsResponseDto;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.SellerOption;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.SellerOptionsRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.InventoryNotFoundException;
import com.academy.projects.ecommerce.ordermanagementservice.services.IDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/inventory/details")
public class DetailsController {
    private final IDetailsService detailsService;

    private final Logger logger = LoggerFactory.getLogger(DetailsController.class);

    @Autowired
    public DetailsController(IDetailsService detailsService) {
        this.detailsService = detailsService;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<DetailsResponseDto> calculate(@RequestBody DetailsRequestDto requestDto) {
        DetailsResponseDto responseDto = detailsService.getDetails(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/sellerOptions")
    public ResponseEntity<List<SellerOption>> getSellerOptions(@RequestBody SellerOptionsRequestDto requestDto) {
        List<SellerOption> sellerOptions = detailsService.getSellerOptions(requestDto.getVariantId(), requestDto.getUserAddress());
        return ResponseEntity.ok(sellerOptions);
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
