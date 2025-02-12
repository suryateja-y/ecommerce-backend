package com.academy.projects.ecommerce.productsearchservice.repositories;

import com.academy.projects.ecommerce.productsearchservice.models.Seller;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends ElasticsearchRepository<Seller, String> {
    Optional<Seller> findBySellerId(String sellerId);
}
