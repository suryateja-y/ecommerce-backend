package com.academy.projects.ecommerce.inventorymanagementservice.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotBlank(message = "Zip Code should be provided!!!")
    private String zip;
    @NotBlank(message = "Country should be provided!!!")
    private String country;
}
