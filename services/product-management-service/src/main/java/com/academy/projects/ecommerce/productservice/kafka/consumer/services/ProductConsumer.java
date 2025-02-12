package com.academy.projects.ecommerce.productservice.kafka.consumer.services;

import com.academy.projects.ecommerce.productservice.kafka.dtos.ProductDto;
import com.academy.projects.ecommerce.productservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productservice.models.Product;
import com.academy.projects.ecommerce.productservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {
    private final IProductService productService;

    @Autowired
    public ProductConsumer(IProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "${application.kafka.topics.product-topic}", groupId = "${application.kafka.consumer.product-group}", containerFactory = "productKafkaListenerContainerFactory")
    public void consumer(ProductDto productDto) {
        Product product = productDto.getProduct();
        if(!product.getApprovalStatus().equals(ApprovalStatus.APPROVED))
            productService.deleteProduct(product);
        else
            productService.consumeProduct(product);
    }
}
