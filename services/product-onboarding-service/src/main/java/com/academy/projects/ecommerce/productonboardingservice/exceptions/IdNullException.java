package com.academy.projects.ecommerce.productonboardingservice.exceptions;

import lombok.ToString;

@ToString
public class IdNullException extends RuntimeException {
    public IdNullException() {
        super("Provide id is null!!!");
    }
}
