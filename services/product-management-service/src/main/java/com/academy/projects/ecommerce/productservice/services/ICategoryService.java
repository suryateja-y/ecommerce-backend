package com.academy.projects.ecommerce.productservice.services;

import com.academy.projects.ecommerce.productservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productservice.models.Category;

import java.util.List;

public interface ICategoryService {
    Category addCategory(Category category);
    Category updateCategory(Category category);
    Category getCategory(String id);
    void invalidateCategory(String id);
    Category activateCategory(String id);
    List<Category> getCategories(int page, int pageSize, ApprovalStatus approvalStatus);
}