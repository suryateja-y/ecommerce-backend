package com.academy.projects.ecommerce.productonboardingservice.repositories;

import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findByCategoryNameIgnoreCaseAndHighLevelCategoryIgnoreCase(String categoryName, String highLevelCategory);
    Page<Category> findAllByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);
}
