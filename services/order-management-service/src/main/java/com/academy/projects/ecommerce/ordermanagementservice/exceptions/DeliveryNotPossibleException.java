package com.academy.projects.ecommerce.ordermanagementservice.exceptions;

import lombok.ToString;

@ToString
public class DeliveryNotPossibleException extends RuntimeException {
    public DeliveryNotPossibleException(String userCountry, String sellerCountry) {
        super("Delivery is not possible from country: '" + sellerCountry + "' to the country: '" + userCountry + "'!!!");
    }

    public DeliveryNotPossibleException(String reason) {
        super(reason);
    }
}
