package com.academy.projects.ecommerce.productservice.exceptions;

import lombok.ToString;

@ToString
public class AttributeNotFoundException extends RuntimeException {
    public AttributeNotFoundException(String attribute) {
        super("Attribute: '" + attribute + "' not found!!!");
    }
}
