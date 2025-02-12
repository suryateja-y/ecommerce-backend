package com.academy.projects.ecommerce.productservice.exceptions;

import com.academy.projects.ecommerce.productservice.models.Attribute;
import lombok.ToString;

@ToString
public class AttributeNotProvidedException extends RuntimeException {
    public AttributeNotProvidedException(Attribute expectedAttribute) {
        super("Attribute '" + expectedAttribute.getName() + "' is not provided!!!");
    }
}
