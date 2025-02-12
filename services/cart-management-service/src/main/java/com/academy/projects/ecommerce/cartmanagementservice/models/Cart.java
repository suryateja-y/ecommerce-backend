package com.academy.projects.ecommerce.cartmanagementservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Document(collection = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseModel {
    public static final String CART_SEQUENCE = "Cart Sequence";
    private String userId;
    private Set<CartUnit> cartUnits = new LinkedHashSet<>();
    private BigDecimal subTotal = BigDecimal.ZERO;

}
