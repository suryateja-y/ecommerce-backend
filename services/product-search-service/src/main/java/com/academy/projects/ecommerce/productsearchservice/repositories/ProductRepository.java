package com.academy.projects.ecommerce.productsearchservice.repositories;

import com.academy.projects.ecommerce.productsearchservice.models.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    Optional<Product> findByProductId(String productId);
}
