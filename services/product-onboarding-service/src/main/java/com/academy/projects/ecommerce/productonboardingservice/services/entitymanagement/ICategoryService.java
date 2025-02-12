package com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement;

import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(Category category);
    Category updateCategory(Category category);
    Category getCategory(String id);
    void invalidateCategory(String id);
    Category activateCategory(String id);
    List<Category> getCategories(int page, int pageSize, ApprovalStatus approvalStatus);
}
