package com.academy.projects.ecommerce.inventorymanagementservice.services;

import com.academy.projects.ecommerce.inventorymanagementservice.dtos.DetailsRequestDto;
import com.academy.projects.ecommerce.inventorymanagementservice.dtos.DetailsResponseDto;
import com.academy.projects.ecommerce.inventorymanagementservice.dtos.SellerOption;
import com.academy.projects.ecommerce.inventorymanagementservice.models.Address;

import java.util.List;

public interface IDetailsService {
    DetailsResponseDto getDetails(DetailsRequestDto requestDto);
    List<SellerOption> getSellerOptions(String variantId, Address userAddress);
}
