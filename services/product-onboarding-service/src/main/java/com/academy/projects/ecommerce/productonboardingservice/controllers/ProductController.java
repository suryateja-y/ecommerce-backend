package com.academy.projects.ecommerce.productonboardingservice.controllers;

import com.academy.projects.ecommerce.productonboardingservice.dtos.CreateUpdateResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.dtos.InternalResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.ProductAlreadyExistsException;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.ProductNotFoundException;
import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.Product;
import com.academy.projects.ecommerce.productonboardingservice.models.InternalResponseStatus;
import com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/product-onboardings/products")
public class ProductController {

    private final IProductService productService;

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<CreateUpdateResponseDto> createProduct(@RequestBody Product product) {
        CreateUpdateResponseDto responseDto = new CreateUpdateResponseDto();
        InternalResponseDto internalResponseDto = productService.addProduct(product);
        return respond(responseDto, internalResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {
        Product product = productService.getProduct(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) ApprovalStatus approvalStatus) {
        List<Product> products = productService.getProducts(page, pageSize, approvalStatus);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PatchMapping("")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<CreateUpdateResponseDto> updateProduct(@RequestBody Product product) {
        CreateUpdateResponseDto responseDto = new CreateUpdateResponseDto();
        InternalResponseDto internalResponseDto = productService.updateProduct(product);
        return respond(responseDto, internalResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<CreateUpdateResponseDto> deleteProduct(@PathVariable String id) {
        CreateUpdateResponseDto responseDto = new CreateUpdateResponseDto();
        InternalResponseDto internalResponseDto = productService.deleteProduct(id);
        return respond(responseDto, internalResponseDto, HttpStatus.OK);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<String> handle(Exception exception) {
        logger.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(Exception exception) {
        logger.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException exception) {
        logger.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<CreateUpdateResponseDto> respond(CreateUpdateResponseDto responseDto, InternalResponseDto internalResponseDto, HttpStatus httpStatus) {
        if(internalResponseDto.getResponseStatus() == InternalResponseStatus.SUCCESS) {
            responseDto.setMessage(internalResponseDto.getMessage());
            responseDto.setCreatedOrUpdatedDate(internalResponseDto.getRespondedAt());
            responseDto.setId(internalResponseDto.getId());
            return ResponseEntity.status(httpStatus).body(responseDto);
        } else {
            responseDto.setMessage(internalResponseDto.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
