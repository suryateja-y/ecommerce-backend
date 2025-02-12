package com.academy.projects.ecommerce.usermanagementservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "addresses")
@Getter
@Setter
@RequiredArgsConstructor
public class Address extends IDGenerationBaseModel {
    @NotBlank(message = "House number should be provided!!!")
    private String houseNumber;
    @NotBlank(message = "Street should be provided!!!")
    private String street;
    private String addressLine1;
    private String addressLine2;
    @NotBlank(message = "City should be provided!!!")
    private String city;
    @NotBlank(message = "State should be provided!!!")
    private String state;
    @NotBlank(message = "Zip Code should be provided!!!")
    private String zip;
    @NotBlank(message = "Country should be provided!!!")
    private String country;
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
}
