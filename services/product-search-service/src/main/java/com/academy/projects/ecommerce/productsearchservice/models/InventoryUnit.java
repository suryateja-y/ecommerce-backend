package com.academy.projects.ecommerce.productsearchservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.math.BigDecimal;

@Document(indexName = "inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUnit extends BaseModel implements Serializable {

    @Transient
    @CustomUpdate
    @JsonIgnore
    public static final String SEQUENCE_NAME = "inventory_sequence";
    private String inventoryId;
    private String productId;
    private String variantId;
    private String sellerId;
    private Long quantity;
    private BigDecimal unitPrice;
}
