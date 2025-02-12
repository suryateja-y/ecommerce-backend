package com.academy.projects.ecommerce.productonboardingservice.controllers;

import com.academy.projects.ecommerce.productonboardingservice.dtos.CreateUpdateResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.dtos.GetResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.Category;
import com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/product-onboardings/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @Autowired
    public CategoryController(final ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<CreateUpdateResponseDto> createCategory(@RequestBody Category category) {
        CreateUpdateResponseDto createUpdateResponseDto = new CreateUpdateResponseDto();
        try {
            Category savedCategory = categoryService.createCategory(category);
            createUpdateResponseDto.setId(savedCategory.getId());
            createUpdateResponseDto.setMessage("Category created successfully!!!");
            createUpdateResponseDto.setCreatedOrUpdatedDate(savedCategory.getCreatedAt());
            return new ResponseEntity<>(createUpdateResponseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            createUpdateResponseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(createUpdateResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<CreateUpdateResponseDto> updateCategory(@RequestBody Category category) {
        CreateUpdateResponseDto createUpdateResponseDto = new CreateUpdateResponseDto();
        try {
            Category savedCategory = categoryService.updateCategory(category);
            createUpdateResponseDto.setId(savedCategory.getId());
            createUpdateResponseDto.setMessage("Category updated successfully!!!");
            createUpdateResponseDto.setCreatedOrUpdatedDate(savedCategory.getModifiedAt());
            return new ResponseEntity<>(createUpdateResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            createUpdateResponseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(createUpdateResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<GetResponseDto> getCategory(@PathVariable String categoryId) {
        GetResponseDto getResponseDto = new GetResponseDto();
        try {
            Category category = categoryService.getCategory(categoryId);
            getResponseDto.setCategory(category);
            getResponseDto.setMessage("Category retrieved successfully!!!");
            getResponseDto.setRetrivedDate(new Date());
            return new ResponseEntity<>(getResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            getResponseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(getResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<List<Category>> getAllCategories(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) ApprovalStatus approvalStatus) {
        List<Category> categories = categoryService.getCategories(page, pageSize, approvalStatus);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<CreateUpdateResponseDto> invalidateCategory(@PathVariable String categoryId) {
        CreateUpdateResponseDto createUpdateResponseDto = new CreateUpdateResponseDto();
        try {
            categoryService.invalidateCategory(categoryId);
            createUpdateResponseDto.setMessage("Category invalidated successfully!!!");
            createUpdateResponseDto.setId(categoryId);
            createUpdateResponseDto.setCreatedOrUpdatedDate(new Date());
            return new ResponseEntity<>(createUpdateResponseDto, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            createUpdateResponseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(createUpdateResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<CreateUpdateResponseDto> activateCategory(@PathVariable String categoryId) {
        CreateUpdateResponseDto createUpdateResponseDto = new CreateUpdateResponseDto();
        try {
            Category savedCategory = categoryService.activateCategory(categoryId);
            createUpdateResponseDto.setId(savedCategory.getId());
            createUpdateResponseDto.setMessage("Category activated successfully!!!");
            createUpdateResponseDto.setCreatedOrUpdatedDate(savedCategory.getModifiedAt());
            return new ResponseEntity<>(createUpdateResponseDto, HttpStatus.OK);
        } catch (Exception e) {
            createUpdateResponseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(createUpdateResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
