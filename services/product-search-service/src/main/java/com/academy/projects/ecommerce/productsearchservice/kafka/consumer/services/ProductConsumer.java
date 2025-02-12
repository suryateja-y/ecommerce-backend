package com.academy.projects.ecommerce.productsearchservice.kafka.consumer.services;

import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.AttributeDto;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.ProductActionDto;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.ProductContainer;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.ProductDto;
import com.academy.projects.ecommerce.productsearchservice.models.Product;
import com.academy.projects.ecommerce.productsearchservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductConsumer {

    private final IProductService productService;

    @Autowired
    public ProductConsumer(IProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "${application.kafka.topics.product-update-topic}", groupId = "${application.kafka.consumer.product-update-group}", containerFactory = "kafkaListenerContainerFactoryForProduct")
    public void consumer(ProductDto productDto) {
        try {
            productService.update(from(productDto));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ProductActionDto from(ProductDto productDto) {
        ProductActionDto productActionDto = new ProductActionDto();
        productActionDto.setProduct(from(productDto.getProduct()));
        productActionDto.setAction(productDto.getAction());
        return productActionDto;
    }

    private Product from (ProductContainer productContainer) {
        Product product = new Product();
        product.setName(productContainer.getName());
        product.setDescription(productContainer.getDescription());
        product.setCategory(productContainer.getCategory());
        product.setProductId(productContainer.getId());
        product.setCompany(productContainer.getCompany());
        product.setImage(productContainer.getImage());
        product.setDimensionalMetrics(productContainer.getDimensionalMetrics());
        product.setAttributes(from(productContainer.getAttributes()));
        return product;
    }

    private Map<String, String> from(List<AttributeDto> attributeDtos) {
        Map<String, String> attributes = new LinkedHashMap<>();
        for (AttributeDto attributeDto : attributeDtos)
            attributes.put(attributeDto.getName(), Objects.toString(attributeDto.getValue()));
        return attributes;
    }
}
