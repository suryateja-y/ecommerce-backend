package com.academy.projects.ecommerce.productservice.repositories;

import com.academy.projects.ecommerce.productservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productservice.models.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findAllByEntityState(ApprovalStatus approvalStatus, Pageable pageable);
}
