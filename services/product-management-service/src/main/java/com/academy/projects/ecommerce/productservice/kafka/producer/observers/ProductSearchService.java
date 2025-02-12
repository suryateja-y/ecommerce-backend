package com.academy.projects.ecommerce.productservice.kafka.producer.observers;

import com.academy.projects.ecommerce.productservice.kafka.dtos.ProductDto;
import com.academy.projects.ecommerce.productservice.kafka.dtos.VariantDto;
import com.academy.projects.ecommerce.productservice.kafka.producer.product.ProductObserver;
import com.academy.projects.ecommerce.productservice.kafka.producer.variant.VariantObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductSearchService implements ProductObserver, VariantObserver {
    private final KafkaTemplate<String, ProductDto> productKafkaTemplate;
    private final KafkaTemplate<String, VariantDto> variantKafkaTemplate;

    @Value("${application.kafka.topics.approved-product-topic}")
    private String productSearchProductTopic;

    @Value("${application.kafka.topics.approved-variant-topic}")
    private String productSearchVariantTopic;

    @Autowired
    public ProductSearchService(KafkaTemplate<String, ProductDto> productKafkaTemplate, KafkaTemplate<String, VariantDto> variantKafkaTemplate) {
        this.productKafkaTemplate = productKafkaTemplate;
        this.variantKafkaTemplate = variantKafkaTemplate;
    }

    @Override
    public void sendUpdate(ProductDto productDto) {
        productKafkaTemplate.send(productSearchProductTopic, productDto);
    }

    @Override
    public void sendUpdate(VariantDto variantDto) {
        variantKafkaTemplate.send(productSearchVariantTopic, variantDto);
    }
}
