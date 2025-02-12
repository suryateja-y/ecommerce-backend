package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.VariantActionDto;
import com.academy.projects.ecommerce.productsearchservice.models.Product;
import com.academy.projects.ecommerce.productsearchservice.models.Variant;

public interface IVariantService {
    void update(VariantActionDto variantActionDto);
    Variant getByVariantId(Product product, String variantId);
}
