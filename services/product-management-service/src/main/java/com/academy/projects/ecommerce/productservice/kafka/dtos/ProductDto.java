package com.academy.projects.ecommerce.productservice.kafka.dtos;

import com.academy.projects.ecommerce.productservice.models.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto implements Serializable {
    private Product product;
    private Action action;
}
