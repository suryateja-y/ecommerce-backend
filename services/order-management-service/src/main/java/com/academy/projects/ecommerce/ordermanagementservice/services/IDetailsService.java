package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.dtos.*;
import com.academy.projects.ecommerce.ordermanagementservice.models.Address;
import com.academy.projects.ecommerce.ordermanagementservice.models.DeliveryFeasibility;
import com.academy.projects.ecommerce.ordermanagementservice.models.OrderItem;

import java.util.List;

public interface IDetailsService {
    DetailsResponseDto getDetails(DetailsRequestDto requestDto);
    List<SellerOption> getSellerOptions(String variantId, Address userAddress);
    DeliveryFeasibility checkFeasibilityAndETA(OrderItem orderItem, Address customerAddress);
}
