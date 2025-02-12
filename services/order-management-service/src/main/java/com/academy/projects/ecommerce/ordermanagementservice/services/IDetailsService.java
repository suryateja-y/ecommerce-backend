package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.DetailsRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.DetailsResponseDto;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.SellerOption;
import com.academy.projects.ecommerce.ordermanagementservice.models.Address;

import java.util.List;

public interface IDetailsService {
    DetailsResponseDto getDetails(DetailsRequestDto requestDto);
    List<SellerOption> getSellerOptions(String variantId, Address userAddress);
}
