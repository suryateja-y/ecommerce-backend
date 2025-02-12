package com.academy.projects.ecommerce.productservice.services;

import com.academy.projects.ecommerce.productservice.models.Variant;

public interface IVariantService {
    void consumeVariant(Variant variant);
    void deleteVariant(Variant variant);
}
