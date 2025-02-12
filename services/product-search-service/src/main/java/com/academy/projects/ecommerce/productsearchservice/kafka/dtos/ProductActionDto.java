package com.academy.projects.ecommerce.productsearchservice.kafka.dtos;

import com.academy.projects.ecommerce.productsearchservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductActionDto implements Serializable {
    private Product product;
    private Action action;
}
