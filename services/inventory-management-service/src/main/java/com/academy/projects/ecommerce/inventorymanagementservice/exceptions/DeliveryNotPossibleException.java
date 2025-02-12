package com.academy.projects.ecommerce.inventorymanagementservice.exceptions;

import lombok.ToString;

@ToString
public class DeliveryNotPossibleException extends RuntimeException {
    public DeliveryNotPossibleException(String userCountry, String sellerCountry) {
        super("Delivery is not possible from country: '" + sellerCountry + "' to the country: '" + userCountry + "'!!!");
    }
}
