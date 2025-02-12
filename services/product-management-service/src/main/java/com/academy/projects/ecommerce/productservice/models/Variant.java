package com.academy.projects.ecommerce.productservice.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Variant extends BaseModel implements Serializable {
    @DBRef(lazy = true)
    private Product product;

    private List<Attribute> attributes;

    @CustomUpdate
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_FOR_APPROVAL;
}
