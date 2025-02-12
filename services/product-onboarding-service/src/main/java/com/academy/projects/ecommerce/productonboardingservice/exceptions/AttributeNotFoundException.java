package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class AttributeNotFoundException extends RuntimeException {
    public AttributeNotFoundException(String name) {
        super("Attribute: " + name + " not provided!!!");
    }
}
