package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryFeasibilityDetails;
import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryFeasibilityItem;
import com.academy.projects.ecommerce.productsearchservice.models.Address;

import java.util.List;

public interface IDetailsService {
    List<DeliveryFeasibilityDetails> get(List<DeliveryFeasibilityItem> items, Address customerAddress);
}
