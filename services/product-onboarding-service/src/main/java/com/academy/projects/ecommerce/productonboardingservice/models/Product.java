package com.academy.projects.ecommerce.productonboardingservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Document(collection = "products")
@Getter
@Setter
@RequiredArgsConstructor
public class Product extends BaseModel implements Serializable {

    @Transient
    @CustomUpdate
    @JsonIgnore
    public static final String SEQUENCE_NAME = "product_sequence";

    @NotBlank(message = "Product Name should be provided!!!")
    private String name;

    @NotBlank(message = "Product Description should be provided!!!")
    private String description;

    @NotBlank(message = "Price should be provided!!!")
    @Positive
    private BigDecimal price;

    @NotBlank(message = "Company name should be provided!!!")
    private String company;

    @NotBlank(message = "Brand name should be provided!!!")
    private String brandName;

    @NotNull(message = "Images of the product should be provided!!!")
    @DocumentReference(lazy = true)
    @NotEmpty(message = "Atleast one image of the product should be!!!")
    private List<Image> image;

    @NotNull(message = "Shipping Details should be provided")
    private DimensionalMetrics dimensionalMetrics;

    @DBRef(lazy = true)
    @NotNull(message = "Category should be provided")
    @CustomUpdate
    private Category category;

    @NotNull(message = "Images of the product should be provided!!!")
    @CustomUpdate
    private List<Attribute> attributes;

    @CustomUpdate
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_FOR_APPROVAL;

    @CustomUpdate
    private String approvalId;

    @DBRef(lazy = true)
    private List<Variant> variants;
}
