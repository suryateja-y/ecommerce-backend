package com.academy.projects.ecommerce.productservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Document(collection = "products")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Product extends BaseModel implements Serializable {

    @NotBlank(message = "Product Name should be provided!!!")
    private String name;

    @NotBlank(message = "Product Description should be provided!!!")
    private String description;

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
    private Category category;

    @NotNull(message = "Images of the product should be provided!!!")
    private List<Attribute> attributes;

    @DocumentReference(lazy = true)
    private List<Variant> variants = new LinkedList<>();

    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_FOR_APPROVAL;
}
