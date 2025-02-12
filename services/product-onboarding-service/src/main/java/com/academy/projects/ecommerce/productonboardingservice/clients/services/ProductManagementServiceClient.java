package com.academy.projects.ecommerce.productonboardingservice.clients.services;

import com.academy.projects.ecommerce.productonboardingservice.models.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${application.services.product-management-service}")
public interface ProductManagementServiceClient {

    @PostMapping("/api/${application.version}/products/categories")
    void createCategory(@RequestBody Category category);

    @PutMapping("/api/${application.version}/products/categories")
    void updateCategory(@RequestBody Category savedCategory);

    @DeleteMapping("/api/${application.version}/products/categories/{id}")
    void invalidateCategory(@PathVariable String id);

    @PutMapping("/api/${application.version}/products/categories/{id}")
    Category activateCategory(@PathVariable String id);


}
