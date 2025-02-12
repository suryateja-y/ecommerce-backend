package com.academy.projects.ecommerce.productonboardingservice.controllers;

import com.academy.projects.ecommerce.productonboardingservice.dtos.CreateUpdateResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.dtos.CreateVariantRequestDto;
import com.academy.projects.ecommerce.productonboardingservice.dtos.InternalResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.VariantAlreadyExistsException;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.VariantNotFoundException;
import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.InternalResponseStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.Variant;
import com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement.IVariantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/product-onboardings/variants")
public class VariantController {

    private final IVariantService variantService;

    private final Logger logger = LoggerFactory.getLogger(VariantController.class);

    public VariantController(final IVariantService variantService) {
        this.variantService = variantService;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<CreateUpdateResponseDto> createVariant(@RequestBody CreateVariantRequestDto requestDto) {
        CreateUpdateResponseDto responseDto = new CreateUpdateResponseDto();
        InternalResponseDto internalResponseDto = variantService.addVariant(requestDto.getProductId(), requestDto.getVariantAttributes());
        return respond(responseDto, internalResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{variantId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<Variant> getVariant(@PathVariable String variantId) {
        Variant variant = variantService.getVariant(variantId);
        return new ResponseEntity<>(variant, HttpStatus.OK);
    }


    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<List<Variant>> getAllProducts(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) ApprovalStatus approvalStatus, @RequestParam(required = false) String productId) {
        List<Variant> variants = variantService.getVariants(page, pageSize, approvalStatus, productId);
        return new ResponseEntity<>(variants, HttpStatus.OK);
    }

    @PatchMapping("")
    @PreAuthorize("hasAnyRole('ROLE_SELLER', 'ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<CreateUpdateResponseDto> updateProduct(@RequestBody CreateVariantRequestDto requestDto) {
        CreateUpdateResponseDto responseDto = new CreateUpdateResponseDto();
        InternalResponseDto internalResponseDto = variantService.updateVariant(requestDto.getProductId(), requestDto.getVariantAttributes());
        return respond(responseDto, internalResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{variantId}")
    @PreAuthorize("hasRole('ROLE_PRODUCT_MANAGER')")
    public ResponseEntity<CreateUpdateResponseDto> deleteVariant(@PathVariable String variantId) {
        CreateUpdateResponseDto responseDto = new CreateUpdateResponseDto();
        InternalResponseDto internalResponseDto = variantService.deleteVariant(variantId);
        return respond(responseDto, internalResponseDto, HttpStatus.OK);
    }

    @ExceptionHandler(VariantAlreadyExistsException.class)
    public ResponseEntity<String> handle(Exception exception) {
        logger.error(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(VariantNotFoundException.class)
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
        if(internalResponseDto.getResponseStatus().equals(InternalResponseStatus.SUCCESS)) {
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
