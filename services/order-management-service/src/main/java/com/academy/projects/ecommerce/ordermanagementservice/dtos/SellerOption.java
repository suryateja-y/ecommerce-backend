package com.academy.projects.ecommerce.ordermanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerOption implements Serializable {
    private String sellerId;
    private Date eta;
    private BigDecimal price;
}
