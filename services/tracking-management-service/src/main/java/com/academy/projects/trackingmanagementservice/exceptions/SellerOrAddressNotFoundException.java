package com.academy.projects.trackingmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class SellerOrAddressNotFoundException extends RuntimeException {
    public SellerOrAddressNotFoundException(String sellerId) {
        super("Seller: '" + sellerId + "' not found!!! Or Seller Address not found!!!");
    }
}
