package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.dtos.DeliveryFeasibilityDetails;
import com.academy.projects.ecommerce.productsearchservice.models.Address;
import com.academy.projects.ecommerce.productsearchservice.models.InventoryUnit;
import com.academy.projects.ecommerce.productsearchservice.models.Seller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ETAService implements IETAService {

    private final ISellerService sellerService;

    private final Logger logger = LoggerFactory.getLogger(ETAService.class);

    @Autowired
    public ETAService(ISellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public DeliveryFeasibilityDetails checkFeasibilityAndETA(InventoryUnit inventoryUnit, Address userAddress) {
        long etaInHours = 0; Date eta = null; boolean isFeasible = false; String reason = "";
        if(inventoryUnit.getQuantity() <= 0) reason = "Variant '" + inventoryUnit.getVariantId() + "' is out of stock of the seller '" + inventoryUnit.getSellerId() + "'";
        Seller seller = sellerService.getBySellerId(inventoryUnit.getSellerId());
        if(!seller.getAddress().getCountry().equalsIgnoreCase(userAddress.getCountry())) reason = "Not Deliverable!!! Seller's Country is different from User's Country";
        else {
            long calculatedEta = calculateEta(userAddress.getZip(), seller.getAddress().getZip());
            if(calculatedEta <= 0) reason = "Not Deliverable!!! Failed to calculate ETA!!!";
            else {
                etaInHours = calculatedEta;
                eta = from(calculatedEta);
                isFeasible = true;
            }
        }

        return DeliveryFeasibilityDetails.builder()
                .etaInHours(etaInHours)
                .eta(eta)
                .isFeasible(isFeasible)
                .reason(reason)
                .sellerId(inventoryUnit.getSellerId())
                .build();
    }

    private static Date from(long etaInHours) {
        Date current = new Date();
        current.setTime(current.getTime() + (etaInHours * 60 * 60 * 1000));
        return current;
    }

    private long calculateEta(String userZipCode, String sellerZipCode) {
        try {
            int[] hours = { 3, 6, 12, 18, 24, 48 };
            long userZip = Long.parseLong(userZipCode);
            long sellerZip = Long.parseLong(sellerZipCode);
            long difference = Math.abs(userZip - sellerZip);
            int index = 0;
            long calculatedEta = 0;
            while(difference > 0) {
                long digit = difference % 10;
                calculatedEta += (hours[index] * digit);
                difference = difference / 10;
                index++;
            }
            if(calculatedEta < 24)
                calculatedEta = 24;
            return calculatedEta;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }
}
