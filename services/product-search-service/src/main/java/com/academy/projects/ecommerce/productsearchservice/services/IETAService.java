package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryFeasibilityDetails;
import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryFeasibilityItem;
import com.academy.projects.ecommerce.productsearchservice.models.Address;
import com.academy.projects.ecommerce.productsearchservice.models.InventoryUnit;

public interface IETAService {
    DeliveryFeasibilityDetails checkFeasibilityAndETA(InventoryUnit inventoryUnit, Address userAddress);

    DeliveryFeasibilityDetails checkFeasibilityAndETA(DeliveryFeasibilityItem item, Address userAddress);
}
