package com.academy.projects.ecommerce.productsearchservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Document(indexName = "products")
@Getter
@Setter
@RequiredArgsConstructor
public class Product extends BaseModel implements Serializable {

    @Transient
    @CustomUpdate
    @JsonIgnore
    public static final String SEQUENCE_NAME = "product_sequence";

    @NotBlank(message = "Product Id should be provided!!!")
    private String productId;

    @NotBlank(message = "Product Name should be provided!!!")
    private String name;

    @NotBlank(message = "Product Description should be provided!!!")
    private String description;

    @NotBlank(message = "Company name should be provided!!!")
    private String company;

    @NotBlank(message = "Brand name should be provided!!!")
    private String brandName;

    @NotNull(message = "Images of the product should be provided!!!")
    @NotEmpty(message = "Atleast one image of the product should be!!!")
    private List<Image> image;

    @NotNull(message = "Shipping Details should be provided")
    private DimensionalMetrics dimensionalMetrics;

    @NotNull(message = "Category should be provided")
    @CustomUpdate
    private Category category;

    @NotNull(message = "Images of the product should be provided!!!")
    @CustomUpdate
    private Map<String, String> attributes;

    @CustomUpdate
    @NotNull(message = "Variants should not be empty")
    private List<Variant> variants = new LinkedList<>();

    @JsonIgnore
    @CustomUpdate
    private Set<String> searchAttributes;
}
