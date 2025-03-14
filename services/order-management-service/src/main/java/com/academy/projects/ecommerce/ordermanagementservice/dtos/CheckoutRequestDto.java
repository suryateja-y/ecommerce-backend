package com.academy.projects.ecommerce.ordermanagementservice.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class CheckoutRequestDto implements Serializable {
    private String addressId;
}
