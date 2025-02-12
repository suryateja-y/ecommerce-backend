package com.academy.projects.ecommerce.paymentmanagementservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    CARD("card"),
    AMAZON_PAY("amazon_pay");
    private final String paymentMethod;
}
