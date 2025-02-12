package com.academy.projects.ecommerce.productsearchservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = "sellers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seller extends BaseModel implements Serializable {

    @Transient
    @CustomUpdate
    @JsonIgnore
    public static final String SEQUENCE_NAME = "seller_sequence";
    private String sellerId;
    private String companyName;
    private String brandName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Address address;
}
