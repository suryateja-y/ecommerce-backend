package com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement;

import com.academy.projects.ecommerce.productonboardingservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.productonboardingservice.configurations.Patcher;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.dtos.InternalResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.*;
import com.academy.projects.ecommerce.productonboardingservice.models.*;
import com.academy.projects.ecommerce.productonboardingservice.repositories.CategoryRepository;
import com.academy.projects.ecommerce.productonboardingservice.repositories.ProductRepository;
import com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement.IProductApprovalManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final IdGenerator idGenerator;
    private final IProductApprovalManager productApprovalManager;
    private final Patcher patcher;

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, IdGenerator idGenerator, IProductApprovalManager productApprovalManager, Patcher patcher) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.idGenerator = idGenerator;
        this.productApprovalManager = productApprovalManager;
        this.patcher = patcher;
    }

    public InternalResponseDto addProduct(Product product) {
        InternalResponseDto internalResponseDto = new InternalResponseDto();
        try {
            if (product.getCategory().getId() == null) throw new CategoryNotProvidedException();
            addCategoryToProduct(product);
            product.setId(idGenerator.getId(Product.SEQUENCE_NAME));
            product.setApprovalStatus(ApprovalStatus.PENDING_FOR_APPROVAL);

            // Sending product for the approval
            String approvalRequestId = this.productApprovalManager.register(product, ActionType.CREATE);
            logger.info("Product Approval Request Id: {}", approvalRequestId);

            product.setApprovalId(approvalRequestId);
            product = productRepository.save(product);

            internalResponseDto.setResponseStatus(InternalResponseStatus.SUCCESS);
            internalResponseDto.setMessage("Product added successfully with id '" + product.getId() + "'!!! Track the approval status using id '" + approvalRequestId + "'");
            internalResponseDto.setId(product.getId());
        } catch(Exception e) {
            internalResponseDto.setMessage(e.getMessage());
            internalResponseDto.setResponseStatus(InternalResponseStatus.FAILURE);
        }
        internalResponseDto.setRespondedAt(new Date());
        return internalResponseDto;
    }

    private List<Attribute> validateAttributes(List<Attribute> requiredAttributes, List<Attribute> actualAttributes) {
        if((actualAttributes == null)) actualAttributes = List.of();
        List<Attribute> attributes = new ArrayList<>();
        for(Attribute requiredAttribute : requiredAttributes) {
            if(!requiredAttribute.isRequired()) continue;
            boolean found = false;
            for(Attribute actualAttribute : actualAttributes) {
                if(requiredAttribute.getName().equals(actualAttribute.getName())) {
                    validateAttribute(requiredAttribute, actualAttribute);
                    attributes.add(actualAttribute);
                    found = true;
                    break;
                }
            }
            if(!found) throw new AttributeNotFoundException(requiredAttribute.getName());
        }
        return attributes;
    }

    private void validateAttribute(Attribute requiredAttribute, Attribute actualAttribute) {
        if(actualAttribute.getValue() == null) throw new AttributeNotValidException("Value is not provided for the attribute: '" + actualAttribute.getName() + "'!!!");
        actualAttribute.setValue(convertType(actualAttribute.getName(), actualAttribute.getValue(), requiredAttribute.getDataType()));
        actualAttribute.setRequired(requiredAttribute.isRequired());
        actualAttribute.setDataType(requiredAttribute.getDataType());
    }

    private Object convertType(String attributeName, Object value, DataType type) {
        try {
            return switch (type) {
                case STRING -> String.valueOf(value);
                case NUMBER -> Integer.parseInt(String.valueOf(value));
                case BOOLEAN -> Boolean.parseBoolean(String.valueOf(value));
                case LIST -> List.of(value);
            };
        } catch (Exception e) {
            throw new AttributeNotValidException("Attribute: '" + attributeName + "' Value is not in the Data Type: " + type + "!!!");
        }
    }

    public Product getProduct(String id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public InternalResponseDto updateProduct(Product product) {
        InternalResponseDto internalResponseDto = new InternalResponseDto();
        try {
            if(product.getId() == null) throw new IdNullException();
            Product savedProduct = getProduct(product.getId());
            patcher.entity(savedProduct, product, Product.class);

            // Updating the Category
            if(product.getCategory() != null) {
                addCategoryToProduct(product);
            }

            // Updating attributes
            if(product.getAttributes() != null) this.updateAttributes(product, savedProduct, savedProduct.getCategory().getAttributes());

            savedProduct.setApprovalStatus(ApprovalStatus.PENDING_FOR_APPROVAL);

            savedProduct = productRepository.save(savedProduct);

            // Sending product for the approval
            String approvalRequestId = this.productApprovalManager.register(savedProduct, ActionType.UPDATE);

            // Send notification with approval request details
            logger.info("Product Update Approval Request Id: {}", approvalRequestId);

            internalResponseDto.setResponseStatus(InternalResponseStatus.SUCCESS);
            internalResponseDto.setMessage("Product: '" + product.getId() + "' updated successfully!!! Track the approval status using id '" + approvalRequestId + "'");
        } catch(Exception e) {
            internalResponseDto.setMessage(e.getMessage());
            internalResponseDto.setResponseStatus(InternalResponseStatus.FAILURE);
        }
        internalResponseDto.setRespondedAt(new Date());
        return internalResponseDto;
    }

    @Override
    public InternalResponseDto deleteProduct(String id) {
        InternalResponseDto internalResponseDto = new InternalResponseDto();
        try {
            Product productToDelete = getProduct(id);

            // Sending product for the deletion approval
            String approvalRequestId = this.productApprovalManager.register(productToDelete, ActionType.DELETE);

            // Send notification with approval request details
            logger.info("Product Delete Approval Request Id: {}", approvalRequestId);

            internalResponseDto.setResponseStatus(InternalResponseStatus.SUCCESS);
            internalResponseDto.setMessage("Product: '" + productToDelete.getId() + "' has been send for deletion successfully!!! Track the approval status using id '" + approvalRequestId + "'");
        } catch(Exception e) {
            internalResponseDto.setMessage(e.getMessage());
            internalResponseDto.setResponseStatus(InternalResponseStatus.FAILURE);
        }
        return internalResponseDto;
    }

    @Override
    public List<Product> getProducts(int page, int pageSize, ApprovalStatus approvalStatus) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if(approvalStatus != null) return this.getProductsByApprovalStatus(approvalStatus, pageable);
        return productRepository.findAll(pageable).getContent();
    }

    private List<Product> getProductsByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable) {
        return productRepository.findAllByApprovalStatus(approvalStatus, pageable);
    }

    private void addCategoryToProduct(Product product) {
        Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new CategoryNotFoundException(product.getCategory().getId()));
        List<Attribute> requiredAttributes = category.getAttributes();
        List<Attribute> actualAttributes = product.getAttributes();
        List<Attribute> attributesToAdd = this.validateAttributes(requiredAttributes, actualAttributes);
        product.setAttributes(attributesToAdd);
    }

    private void updateAttributes(Product product, Product productToUpdate, List<Attribute> allowedAttributes) {
        for(Attribute requiredAttribute : product.getAttributes()) {
            Attribute baseAttribute = this.getAttributeIfAllowed(requiredAttribute, allowedAttributes);
            if(baseAttribute != null) {
                Attribute addedAttribute = this.getAddedAttribute(requiredAttribute, productToUpdate.getAttributes());
                addedAttribute.setName(baseAttribute.getName());
                addedAttribute.setRequired(baseAttribute.isRequired());
                addedAttribute.setDataType(baseAttribute.getDataType());
                addedAttribute.setValue(requiredAttribute.getValue());
                this.updateAttribute(addedAttribute, productToUpdate);
            }
        }
    }

    private void updateAttribute(Attribute addedAttribute, Product productToUpdate) {
        for(Attribute attribute : productToUpdate.getAttributes()) {
            if(attribute.getName().equals(addedAttribute.getName())) {
                attribute.setValue(addedAttribute.getValue());
                attribute.setRequired(addedAttribute.isRequired());
                attribute.setDataType(addedAttribute.getDataType());
                return;
            }
        }
        productToUpdate.getAttributes().add(addedAttribute);
    }

    private Attribute getAttributeIfAllowed(Attribute attribute, List<Attribute> allowedAttributes) {
        for(Attribute attributeToCheck : allowedAttributes) {
            if(attribute.getName().equals(attributeToCheck.getName())) return attributeToCheck;
        }
        return null;
    }
    private Attribute getAddedAttribute(Attribute attribute, List<Attribute> currentAttributes) {
        for(Attribute attributeToCheck : currentAttributes) {
            if(attribute.getName().equals(attributeToCheck.getName())) return attributeToCheck;
        }
        return new Attribute();
    }
}
