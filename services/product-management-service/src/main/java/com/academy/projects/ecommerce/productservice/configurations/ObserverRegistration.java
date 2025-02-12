package com.academy.projects.ecommerce.productservice.configurations;

import com.academy.projects.ecommerce.productservice.kafka.producer.observers.ProductSearchService;
import com.academy.projects.ecommerce.productservice.kafka.producer.product.ProductUpdateManager;
import com.academy.projects.ecommerce.productservice.kafka.producer.variant.VariantUpdateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverRegistration {
    private final ProductUpdateManager productUpdateManager;
    private final VariantUpdateManager variantUpdateManager;
    private final ProductSearchService productSearchService;

    @Autowired
    public ObserverRegistration(ProductUpdateManager productUpdateManager, VariantUpdateManager variantUpdateManager, ProductSearchService productSearchService) {
        this.productUpdateManager = productUpdateManager;
        this.variantUpdateManager = variantUpdateManager;
        this.productSearchService = productSearchService;
    }

    @Bean
    public boolean registerProductUpdateObservers() {
        productUpdateManager.addObserver(productSearchService);
        return true;
    }

    @Bean
    public boolean registerVariantUpdateObservers() {
        variantUpdateManager.addObserver(productSearchService);
        return true;
    }
}
