package com.academy.projects.ecommerce.productservice.services;

import com.academy.projects.ecommerce.productservice.dtos.GetProductRequestDto;
import com.academy.projects.ecommerce.productservice.dtos.DetailedProductDto;
import com.academy.projects.ecommerce.productservice.models.Product;
import com.academy.projects.ecommerce.productservice.models.Variant;

public interface IProductService {
    void deleteProduct(Product product);
    void consumeProduct(Product product);
    @SuppressWarnings("UnusedReturnValue")
    Product addVariant(Variant variant);
    DetailedProductDto get(GetProductRequestDto requestDto);
}
