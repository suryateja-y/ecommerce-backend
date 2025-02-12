package com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement;

import com.academy.projects.ecommerce.productonboardingservice.dtos.InternalResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productonboardingservice.models.Attribute;
import com.academy.projects.ecommerce.productonboardingservice.models.Variant;

import java.util.List;

public interface IVariantService {
    InternalResponseDto addVariant(String productId, List<Attribute> variantAttributes);
    Variant getVariant(String variantId);
    List<Variant> getVariants(int page, int pageSize, ApprovalStatus approvalStatus, String productId);
    InternalResponseDto updateVariant(String productId, List<Attribute> variantAttributes);
    InternalResponseDto deleteVariant(String variantId);
}
