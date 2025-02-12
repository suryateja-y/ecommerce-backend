package com.academy.projects.ecommerce.productservice.kafka.producer.variant;

import com.academy.projects.ecommerce.productservice.kafka.dtos.VariantDto;

public interface VariantObserver {
    void sendUpdate(VariantDto variantDto);
}
