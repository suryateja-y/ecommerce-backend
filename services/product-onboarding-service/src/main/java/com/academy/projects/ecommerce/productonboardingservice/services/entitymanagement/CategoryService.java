package com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement;

import com.academy.projects.ecommerce.productonboardingservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.productonboardingservice.configurations.Patcher;
import com.academy.projects.ecommerce.productonboardingservice.clients.services.ProductManagementServiceClient;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.CategoryAlreadyExistsException;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.CategoryNotFoundException;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.CategoryRegistrationFailedException;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.IdNullException;
import com.academy.projects.ecommerce.productonboardingservice.kafka.producer.services.CategoryProducer;
import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.Attribute;
import com.academy.projects.ecommerce.productonboardingservice.models.Category;
import com.academy.projects.ecommerce.productonboardingservice.repositories.CategoryRepository;
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
    private final ProductManagementServiceClient productServiceClient;
    private final Patcher updatePatcher;
    private final IAttributeService attributeService;
    private final CategoryProducer categoryProducer;

    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    public CategoryService(final CategoryRepository categoryRepository, final IdGenerator idGenerator, final ProductManagementServiceClient productServiceClient, final Patcher updatePatcher, IAttributeService attributeService, CategoryProducer categoryProducer) {
        this.categoryRepository = categoryRepository;
        this.idGenerator = idGenerator;
        this.productServiceClient = productServiceClient;
        this.updatePatcher = updatePatcher;
        this.attributeService = attributeService;
        this.categoryProducer = categoryProducer;
    }

    @Override
    public Category createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryNameIgnoreCaseAndHighLevelCategoryIgnoreCase(category.getCategoryName(), category.getHighLevelCategory()).orElse(null);
        if(savedCategory != null) throw new CategoryAlreadyExistsException(category.getCategoryName(), category.getHighLevelCategory());

        if(category.getId() == null)
            category.setId(idGenerator.getId(Category.SEQUENCE_NAME));
        category.setApprovalStatus(ApprovalStatus.APPROVED);

        try {
            // Send to Product Service
            productServiceClient.createCategory(category);
            category.setCreatedAt(new Date());
            category = categoryRepository.save(category);
            logger.info("Category: {} created successfully!!!", category);

            // Send update to all the Sellers and Product Managers
            categoryProducer.send(category, ActionType.CREATE);

            return category;
        } catch (Exception e) {
            throw new CategoryRegistrationFailedException(category.getId(), e.getMessage());
        }
    }

    @Override
    public Category updateCategory(Category category) {
        if(category.getId() == null) throw new IdNullException();
        Category savedCategory = categoryRepository.findById(category.getId()).orElseThrow(() -> new CategoryNotFoundException(category.getId()));
        try {
            updatePatcher.entity(savedCategory, category, Category.class);
            List<Attribute> finalAttributes = attributeService.merge(savedCategory.getAttributes(), category.getAttributes());
            savedCategory.setAttributes(finalAttributes);
            List<Attribute> finalVariantAttributes = attributeService.merge(savedCategory.getVariantAttributes(), category.getVariantAttributes());
            savedCategory.setVariantAttributes(finalVariantAttributes);
            savedCategory.setModifiedAt(new Date());

            if(category.getHighLevelCategory() != null)
                savedCategory.setHighLevelCategory(category.getHighLevelCategory());
            savedCategory.setApprovalStatus(ApprovalStatus.APPROVED);

            // Send to Product Service
            productServiceClient.createCategory(savedCategory);
            savedCategory = categoryRepository.save(savedCategory);
            logger.info("Category: {} updated successfully!!!", category);

            // Send update to all the Sellers and Product Managers
            categoryProducer.send(savedCategory, ActionType.UPDATE);

            return savedCategory;
        } catch (Exception e) {
            throw new CategoryRegistrationFailedException(category.getId(), e.getMessage());
        }
    }

    @Override
    public Category getCategory(String id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public void invalidateCategory(String id) {
        Category savedCategory = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        try {
            savedCategory.setModifiedAt(new Date());
            savedCategory.setApprovalStatus(ApprovalStatus.REJECTED);
            productServiceClient.invalidateCategory(savedCategory.getId());
            categoryRepository.save(savedCategory);
            logger.info("Category: {} invalidated successfully!!!", savedCategory);

            // Send update to all the Sellers and Product Managers
            categoryProducer.send(savedCategory, ActionType.DELETE);
        } catch (Exception e) {
            throw new CategoryNotFoundException(e.getMessage());
        }
    }

    @Override
    public Category activateCategory(String id) {
        Category savedCategory = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        try {
            savedCategory.setModifiedAt(new Date());
            savedCategory.setApprovalStatus(ApprovalStatus.APPROVED);
            productServiceClient.activateCategory(savedCategory.getId());
            savedCategory = categoryRepository.save(savedCategory);
            logger.info("Category: {} activated successfully!!!", savedCategory);

            // Send update to all the Sellers and Product Managers
            categoryProducer.send(savedCategory, ActionType.CREATE);
            return savedCategory;
        } catch (Exception e) {
            throw new CategoryNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Category> getCategories(int page, int pageSize, ApprovalStatus approvalStatus) {
        if(approvalStatus != null) return this.getCategoriesByApprovalStatus(page, pageSize, approvalStatus);
        Pageable pageable = PageRequest.of(page, pageSize);
        return categoryRepository.findAll(pageable).getContent();
    }

    private List<Category> getCategoriesByApprovalStatus(int page, int pageSize, ApprovalStatus approvalStatus) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return categoryRepository.findAllByApprovalStatus(approvalStatus, pageable).getContent();
    }
}
