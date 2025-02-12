package com.academy.projects.ecommerce.cartmanagementservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartUnit {
    private String productId;
    private String productName;
    private String variantId;
    private String sellerId;
    private int quantity;
    private BigDecimal unitPrice;
    private boolean inStock;
    private Date addedOrModifiedOn;

    @Override
    public boolean equals(Object oCartUnit) {
        if(!(oCartUnit instanceof CartUnit cartUnit)) return false;
        return (this.getVariantId() != null) && (cartUnit.getVariantId() != null) && (this.getVariantId().equals(cartUnit.getVariantId())) && (this.getSellerId() != null) && (cartUnit.getSellerId() != null) && (this.getSellerId().equals(cartUnit.getSellerId()));
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString(){
        return """
                [
                    Variant Id: %s,
                    SellerId: %s,
                    Quantity: %s,
                    UnitPrice: %s,
                    InStock: %s,
                    AddedOrModifiedOn: %s
                ]
                """.formatted(variantId, sellerId, quantity, unitPrice, inStock, addedOrModifiedOn);
    }
}
