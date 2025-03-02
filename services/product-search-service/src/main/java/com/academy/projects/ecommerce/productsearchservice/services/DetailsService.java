package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryFeasibilityDetails;
import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryFeasibilityItem;
import com.academy.projects.ecommerce.productsearchservice.models.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DetailsService implements IDetailsService {
    private final IETAService etaService;

    @Autowired
    public DetailsService(IETAService etaService) {
        this.etaService = etaService;
    }

    @Override
    public List<DeliveryFeasibilityDetails> get(List<DeliveryFeasibilityItem> items, Address customerAddress) {
        List<DeliveryFeasibilityDetails> details = new LinkedList<>();
        for (DeliveryFeasibilityItem item : items) {
            DeliveryFeasibilityDetails detail = etaService.checkFeasibilityAndETA(item, customerAddress);
            details.add(detail);
        }
        return details;
    }
}
