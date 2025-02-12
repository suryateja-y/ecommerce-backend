package com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement;

import com.academy.projects.ecommerce.productonboardingservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.dtos.InternalResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.AttributeNotProvidedException;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.VariantNotFoundException;
import com.academy.projects.ecommerce.productonboardingservice.models.*;
import com.academy.projects.ecommerce.productonboardingservice.repositories.VariantRepository;
import com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement.IVariantApprovalManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VariantService implements IVariantService {
    private final IProductService productService;
    private final VariantRepository variantRepository;
    private final IVariantApprovalManager variantApprovalManager;
    private final IdGenerator idGenerator;

    private final Logger logger = LoggerFactory.getLogger(VariantService.class);

    @Autowired
    public VariantService(IProductService productService, VariantRepository variantRepository, IVariantApprovalManager variantApprovalManager, IdGenerator idGenerator) {
        this.productService = productService;
        this.variantRepository = variantRepository;
        this.variantApprovalManager = variantApprovalManager;
        this.idGenerator = idGenerator;
    }

    @Override
    public InternalResponseDto addVariant(String productId, List<Attribute> variantAttributes) {
        InternalResponseDto responseDto = new InternalResponseDto();
        try {
            Variant variant = new Variant();
            Product product = this.productService.getProduct(productId);
            List<Attribute> expectedAttributes = product.getCategory().getVariantAttributes();
            List<Attribute> attributes = this.analyseAttributes(expectedAttributes, variantAttributes);
            variant.setProduct(product);
            variant.setAttributes(attributes);
            variant.setApprovalStatus(ApprovalStatus.PENDING_FOR_APPROVAL);
            variant.setId(idGenerator.getId(Variant.SEQUENCE_NAME));

            // Sending product for the approval
            String approvalRequestId = this.variantApprovalManager.register(variant, ActionType.CREATE);

            // Send notification with approval request details
            logger.info("Product Approval Request Id: {}", approvalRequestId);


            variant = variantRepository.save(variant);
            responseDto.setMessage("Variant added successfully!!!");
            responseDto.setResponseStatus(InternalResponseStatus.SUCCESS);
            responseDto.setRespondedAt(variant.getCreatedAt());
            responseDto.setId(variant.getId());
        } catch (Exception e) {
            responseDto.setRespondedAt(new Date());
            responseDto.setResponseStatus(InternalResponseStatus.FAILURE);
            responseDto.setMessage(e.getMessage());
        }
        return responseDto;
    }

    private List<Attribute> analyseAttributes(List<Attribute> expectedAttributes, List<Attribute> actualAttributes) {
        List<Attribute> attributes = new ArrayList<>();
        for(Attribute expectedAttribute : expectedAttributes) {
            boolean found = false;
            for(Attribute actualAttribute : actualAttributes) {
                if(expectedAttribute.getName().equals(actualAttribute.getName())) {
                    Attribute newAttribute = new Attribute();
                    newAttribute.setName(expectedAttribute.getName());
                    newAttribute.setValue(actualAttribute.getValue());
                    newAttribute.setRequired(expectedAttribute.isRequired());
                    newAttribute.setDataType(expectedAttribute.getDataType());
                    attributes.add(newAttribute);
                    found = true;
                }
            }
            if(!found) throw new AttributeNotProvidedException(expectedAttribute);
        }
        return attributes;
    }


    @Override
    public Variant getVariant(String variantId) {
        return variantRepository.findById(variantId).orElseThrow(() -> new VariantNotFoundException(variantId));
    }

    @Override
    public List<Variant> getVariants(int page, int pageSize, ApprovalStatus approvalStatus, String productId) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Variant> variantPage;
        if(productId != null) {
            variantPage = variantRepository.findAllByProduct_Id(productId, pageable);
        } else if(approvalStatus.equals(ApprovalStatus.APPROVED)) {
            variantPage = variantRepository.findAllByProduct_ApprovalStatus(approvalStatus, pageable);
        } else {
            variantPage = variantRepository.findAll(pageable);
        }
        return (variantPage == null) ? new ArrayList<>() : variantPage.getContent();
    }

    @Override
    public InternalResponseDto updateVariant(String productId, List<Attribute> variantAttributes) {
        return null;
    }

    @Override
    public InternalResponseDto deleteVariant(String variantId) {
        return null;
    }
}
