package com.academy.projects.ecommerce.productonboardingservice.clients.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserValidityCheckResponseDto implements Serializable {
    private boolean isValid;
    private String message;
}
