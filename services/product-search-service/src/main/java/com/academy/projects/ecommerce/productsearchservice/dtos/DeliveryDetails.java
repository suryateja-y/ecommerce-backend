package com.academy.projects.ecommerce.productsearchservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetails implements Serializable {
    private Boolean inStock = false;
    private Date eta;
    private BigDecimal price;
    private String sellerId;
}
