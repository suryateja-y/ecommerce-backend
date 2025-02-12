package com.academy.projects.ecommerce.productonboardingservice.kafka.producer.services;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.kafka.dtos.ProductDto;
import com.academy.projects.ecommerce.productonboardingservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductProducer {
    @Value("${application.kafka.topics.product-topic}")
    private String productTopic;

    private final KafkaTemplate<String, ProductDto> approvalKafkaTemplate;

    @Autowired
    public ProductProducer(final KafkaTemplate<String, ProductDto> approvalKafkaTemplate) {
        this.approvalKafkaTemplate = approvalKafkaTemplate;
    }

    public void send(ProductDto productDto) {
        approvalKafkaTemplate.send(productTopic, productDto);
    }

    public void send(Product product, String sellerId, ActionType actionType) {
        ProductDto productDto = ProductDto.builder().product(product).sellerId(sellerId).action(actionType).build();
        approvalKafkaTemplate.send(productTopic, productDto);
    }
}
