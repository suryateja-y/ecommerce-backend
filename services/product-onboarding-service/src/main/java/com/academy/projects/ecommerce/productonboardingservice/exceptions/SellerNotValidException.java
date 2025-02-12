package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class SellerNotValidException extends RuntimeException {
    public SellerNotValidException(String message) {
        super("Seller is not valid!!! " + message);
    }
}
