package com.academy.projects.ecommerce.productservice.services;

import com.academy.projects.ecommerce.productservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.productservice.exceptions.CategoryNotFoundException;
import com.academy.projects.ecommerce.productservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productservice.models.Category;
import com.academy.projects.ecommerce.productservice.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final IdGenerator idGenerator;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, IdGenerator idGenerator) {
        this.categoryRepository = categoryRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Category addCategory(Category category) {
        if(category.getId() == null)
            category.setId(idGenerator.getId(Category.SEQUENCE_NAME));
        category.setCreatedDate(new Date());
        category = categoryRepository.save(category);
        logger.info("Category: {} has been created successfully!!!", category);
        // Send notification to the Product Manager
        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        category = categoryRepository.save(category);
        logger.info("Category: {} has been updated successfully!!!", category);
        // Send notification to the Product Manager
        return category;
    }

    @Override
    public Category getCategory(String id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public void invalidateCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        category.setModifiedDate(new Date());
        category.setEntityState(ApprovalStatus.REJECTED);
        categoryRepository.save(category);
        logger.info("Category: {} has been invalidated successfully!!!", category);
        // Send notification to the Product Manager
    }

    @Override
    public Category activateCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        category.setModifiedDate(new Date());
        category.setEntityState(ApprovalStatus.APPROVED);
        category = categoryRepository.save(category);
        logger.info("Category: {} has been activated successfully!!!", category);
        // Send notification to the Product Manager
        return category;
    }

    @Override
    public List<Category> getCategories(int page, int pageSize, ApprovalStatus approvalStatus) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if(approvalStatus != null)
            return categoryRepository.findAllByEntityState(approvalStatus, pageable);
        else
            return categoryRepository.findAll(pageable).getContent();
    }
}
