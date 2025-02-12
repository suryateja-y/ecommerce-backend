package com.academy.projects.ecommerce.productservice.controllers;

import com.academy.projects.ecommerce.productservice.dtos.GetProductRequestDto;
import com.academy.projects.ecommerce.productservice.dtos.DetailedProductDto;
import com.academy.projects.ecommerce.productservice.exceptions.ProductNotFoundException;
import com.academy.projects.ecommerce.productservice.services.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/${application.version}/products")
public class ProductController {

    private final IProductService productService;

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(final IProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<DetailedProductDto> getProduct(@RequestBody GetProductRequestDto requestDto) {
        DetailedProductDto product = productService.get(requestDto);
        return ResponseEntity.ok(product);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ProductNotFoundException> handleProductNotFoundException(final ProductNotFoundException e) {
        logger.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RuntimeException> handleRuntimeException(final RuntimeException e) {
        logger.error(e.getMessage());
        return ResponseEntity.internalServerError().build();
    }
}
