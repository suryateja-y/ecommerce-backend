package com.academy.projects.ecommerce.ordermanagementservice.repositories;

import com.academy.projects.ecommerce.ordermanagementservice.models.InventoryUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryUnit, String> {
    Page<InventoryUnit> findAllBySellerId(String sellerId, Pageable pageable);
    Optional<InventoryUnit> findByIdAndSellerId(String inventoryId, String sellerId);
    Optional<InventoryUnit> findByVariantIdAndSellerId(String variantId, String sellerId);
    List<InventoryUnit> findAllByVariantIdAndQuantityGreaterThan(String variantId, Long quantity);
}
