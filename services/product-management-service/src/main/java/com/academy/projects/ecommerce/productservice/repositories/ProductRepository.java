package com.academy.projects.ecommerce.productservice.repositories;

import com.academy.projects.ecommerce.productservice.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
}
