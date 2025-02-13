package com.academy.projects.ecommerce.paymentmanagementservice.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidationResponse {
    private ValidationResult result;
    private String userId;
}
