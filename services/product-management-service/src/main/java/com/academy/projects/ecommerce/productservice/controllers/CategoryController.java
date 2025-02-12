package com.academy.projects.ecommerce.productservice.controllers;


import com.academy.projects.ecommerce.productservice.dtos.GetResponseDto;
import com.academy.projects.ecommerce.productservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productservice.models.Category;
import com.academy.projects.ecommerce.productservice.services.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/products/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    public CategoryController(final ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Should not be a public API
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    // Should not be a public API
    @PutMapping("")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public Category updateCategory(@RequestBody final Category category) {
        return categoryService.updateCategory(category);
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
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

    // Should not be a public API
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_PRODUCT_MANAGER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCategory(@PathVariable String id) {
        categoryService.invalidateCategory(id);
    }

    // Should not be a public API
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Category activateCategory(@PathVariable String id) {
        return categoryService.activateCategory(id);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<List<Category>> getAllCategories(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) ApprovalStatus approvalStatus) {
        List<Category> categories = categoryService.getCategories(page, pageSize, approvalStatus);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
