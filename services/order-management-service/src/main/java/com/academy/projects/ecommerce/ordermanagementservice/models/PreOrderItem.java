package com.academy.projects.ecommerce.ordermanagementservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "pre_order_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PreOrderItem extends BaseModel {
    private String productId;
    private String productName;
    private String variantId;
    private String sellerId;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    @OneToOne
    private DeliveryFeasibility deliveryFeasibilityDetails;

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object oOrder) {
        if(oOrder instanceof PreOrderItem orderItem)
            return (this.variantId != null) && (orderItem.getVariantId() != null) && this.variantId.equals(orderItem.variantId) && (this.sellerId != null) && (orderItem.getSellerId() != null) && this.sellerId.equals(orderItem.sellerId);
        return false;
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(this.quantity));
    }
}
