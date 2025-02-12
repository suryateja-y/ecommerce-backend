package com.academy.projects.ecommerce.productsearchservice.controllers;

import com.academy.projects.ecommerce.productsearchservice.dtos.ProductSearchDto;
import com.academy.projects.ecommerce.productsearchservice.models.Product;
import com.academy.projects.ecommerce.productsearchservice.services.IProductSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/product-search")
public class SearchController {

    private final IProductSearchService productSearchService;

    private final Logger logger = LoggerFactory.getLogger(SearchController.class);

    public SearchController(final IProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestBody ProductSearchDto searchDto, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<Product> products = productSearchService.search(searchDto, page, pageSize);
        return ResponseEntity.ok(products);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException e) {
        logger.error(e.getMessage());
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
