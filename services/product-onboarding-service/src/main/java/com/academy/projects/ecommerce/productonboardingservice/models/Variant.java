package com.academy.projects.ecommerce.productonboardingservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
import java.util.Objects;

@Document(collection = "variants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Variant extends BaseModel {
    @Transient
    @CustomUpdate
    @JsonIgnore
    public static final String SEQUENCE_NAME = "variants_sequence";

    @DocumentReference
    private Product product;
    private List<Attribute> attributes;

    @CustomUpdate
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_FOR_APPROVAL;

    @CustomUpdate
    private String approvalId;

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if(object == null || getClass() != object.getClass()) return false;
        Variant variant = (Variant) object;
        if(!this.product.getId().equals(variant.product.getId())) return false;
        return Objects.equals(this.attributes, variant.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, attributes);
    }
}
