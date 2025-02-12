package com.academy.projects.ecommerce.productservice.clients.dtos;

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
public class DetailsResponseDto implements Serializable {
    private Date eta;
    private boolean inStock;
    private BigDecimal unitPrice;
}
