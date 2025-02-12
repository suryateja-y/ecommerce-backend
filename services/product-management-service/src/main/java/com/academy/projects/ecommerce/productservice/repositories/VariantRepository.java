package com.academy.projects.ecommerce.productservice.repositories;

import com.academy.projects.ecommerce.productservice.models.Variant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends MongoRepository<Variant, String> {
}
