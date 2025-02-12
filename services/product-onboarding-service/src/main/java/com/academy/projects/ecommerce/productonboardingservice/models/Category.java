package com.academy.projects.ecommerce.productonboardingservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "categories")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Category extends HighLevelCategory implements Serializable {

    @Transient
    @CustomUpdate
    public static final String SEQUENCE_NAME = "categories_sequence";

    @Indexed(unique = true)
    @NotBlank(message = "Category name should be specified!!!")
    private String categoryName;

    @NotBlank(message = "Category description should be mentioned!!!")
    private String categoryDescription;

    @CustomUpdate
    @NotEmpty(message = "Attributes should be mentioned!!!")
    private List<Attribute> attributes;

    @CustomUpdate
    @NotEmpty(message = "Variant Attributes should be mentioned")
    private List<Attribute> variantAttributes;

    @CustomUpdate
    private ApprovalStatus approvalStatus = ApprovalStatus.APPROVED;

}
