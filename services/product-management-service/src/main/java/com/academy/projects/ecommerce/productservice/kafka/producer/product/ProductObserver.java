package com.academy.projects.ecommerce.productservice.kafka.producer.product;

import com.academy.projects.ecommerce.productservice.kafka.dtos.ProductDto;

public interface ProductObserver {
    void sendUpdate(ProductDto productDto);
}
