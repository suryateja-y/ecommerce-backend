package com.academy.projects.ecommerce.productservice.models;

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
    @NotBlank(message = "House number should be provided!!!")
    private String houseNumber;
    @NotBlank(message = "Street should be provided!!!")
    private String street;
    private String AddressLine1;
    private String AddressLine2;
    @NotBlank(message = "City should be provided!!!")
    private String city;
    @NotBlank(message = "State should be provided!!!")
    private String state;
    @NotBlank(message = "Zip Code should be provided!!!")
    private String zip;
    @NotBlank(message = "Country should be provided!!!")
    private String country;
}
