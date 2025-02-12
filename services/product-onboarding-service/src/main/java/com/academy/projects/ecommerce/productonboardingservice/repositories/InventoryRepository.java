package com.academy.projects.ecommerce.productonboardingservice.repositories;

import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.InventoryUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends MongoRepository<InventoryUnit, String> {
    Page<InventoryUnit> findAllBySellerIdAndApprovalStatus(String sellerId, ApprovalStatus approvalStatus, Pageable pageable);
    Page<InventoryUnit> findAllBySellerId(String sellerId, Pageable pageable);
    Optional<InventoryUnit> findByIdAndSellerId(String inventoryId, String sellerId);
    Page<InventoryUnit> findAllByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);
    boolean existsByVariantIdAndSellerId(String productId, String sellerId);
}
