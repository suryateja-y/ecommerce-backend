package com.academy.projects.ecommerce.cartmanagementservice.repositories;

import com.academy.projects.ecommerce.cartmanagementservice.models.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
}
