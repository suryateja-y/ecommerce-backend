package com.academy.projects.ecommerce.productsearchservice.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
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
}
