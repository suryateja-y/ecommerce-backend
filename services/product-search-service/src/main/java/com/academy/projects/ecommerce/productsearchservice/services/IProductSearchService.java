package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.dtos.ProductSearchDto;
import com.academy.projects.ecommerce.productsearchservice.models.Product;

import java.util.List;

public interface IProductSearchService {
    List<Product> search(ProductSearchDto searchDto, int page, int pageSize);
}
