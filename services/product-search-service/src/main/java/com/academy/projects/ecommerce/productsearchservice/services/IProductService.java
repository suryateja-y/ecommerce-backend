package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.ProductActionDto;
import com.academy.projects.ecommerce.productsearchservice.models.Product;
import com.academy.projects.ecommerce.productsearchservice.models.Seller;
import com.academy.projects.ecommerce.productsearchservice.models.Variant;

import java.math.BigDecimal;

public interface IProductService {
    void update(ProductActionDto productDto);
    Product getByProductId(String productId);
    Product update(Product product, Variant variant, Seller seller, BigDecimal unitPrice);
    @SuppressWarnings("UnusedReturnValue")
    Product update(Product product);
}
