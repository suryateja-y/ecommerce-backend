package com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement;

import com.academy.projects.ecommerce.productonboardingservice.dtos.InternalResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.Product;

import java.util.List;

public interface IProductService {
    InternalResponseDto addProduct(Product product);
    Product getProduct(String id);
    InternalResponseDto updateProduct(Product product);
    InternalResponseDto deleteProduct(String id);
    List<Product> getProducts(int page, int pageSize, ApprovalStatus approvalStatus);
}
