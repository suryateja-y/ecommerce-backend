package com.academy.projects.ecommerce.productsearchservice.repositories;

import com.academy.projects.ecommerce.productsearchservice.models.InventoryUnit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends ElasticsearchRepository<InventoryUnit, String> {
    Optional<InventoryUnit> findByVariantIdAndSellerId(String variantId, String sellerId);
    Optional<InventoryUnit> findByInventoryId(String inventoryId);
    List<InventoryUnit> findAllByProductIdAndVariantId(String productId, String variantId);
}
