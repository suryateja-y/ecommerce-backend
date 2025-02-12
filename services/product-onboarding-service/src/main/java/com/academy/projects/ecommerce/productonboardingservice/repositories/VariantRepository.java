package com.academy.projects.ecommerce.productonboardingservice.repositories;

import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.Variant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantRepository extends MongoRepository<Variant, String> {
    Page<Variant> findAllByProduct_Id(String productId, Pageable pageable);
    Page<Variant> findAllByProduct_ApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);
}
