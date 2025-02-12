package com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement;

import com.academy.projects.ecommerce.productonboardingservice.starters.GlobalData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.academy.projects.ecommerce.productonboardingservice.clients.dtos.ApprovalResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.clients.dtos.ResponseStatus;
import com.academy.projects.ecommerce.productonboardingservice.clients.services.ApprovalManagementServiceClient;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.ProductApprovalRegistrationException;
import com.academy.projects.ecommerce.productonboardingservice.kafka.producer.services.ProductProducer;
import com.academy.projects.ecommerce.productonboardingservice.models.Product;
import com.academy.projects.ecommerce.productonboardingservice.repositories.ProductRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductApprovalManager implements IProductApprovalManager {

    @Value("${application.kafka.topics.product-approval-topic}")
    private String productApprovalTopic;

    private final ApprovalManagementServiceClient approvalManagementServiceClient;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final ProductProducer productProducer;

    private final Logger logger = LoggerFactory.getLogger(ProductApprovalManager.class);

    @Autowired
    public ProductApprovalManager(final ApprovalManagementServiceClient approvalManagementServiceClient, final ObjectMapper objectMapper, final ProductRepository productRepository, final ProductProducer productProducer) {
        this.approvalManagementServiceClient = approvalManagementServiceClient;
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.productProducer = productProducer;
    }

    @Override
    public String register(Product product, ActionType actionType) {
        try {
            ApprovalRequest approvalRequest = getApprovalRequest(product, actionType);
            ApprovalResponseDto responseDto = approvalManagementServiceClient.registerForApproval(approvalRequest);
            if(responseDto.getResponseStatus() == ResponseStatus.SUCCESS) {
                product.setApprovalId(responseDto.getApprovalId());

                productProducer.send(product, approvalRequest.getRequester(), actionType);
                return responseDto.getApprovalId();
            } else throw new ProductApprovalRegistrationException(product.getId());
        } catch(Exception e) {
            throw new ProductApprovalRegistrationException(product.getId());
        }
    }

    @Override
    public void updateStatus(ApprovalRequest approvalRequest) {
        try {
            Product product = this.getProduct(approvalRequest);
            Product savedProduct = productRepository.findById(product.getId()).orElse(null);
            if(savedProduct != null) {
                savedProduct.setApprovalStatus(approvalRequest.getStatus());
                savedProduct = productRepository.save(savedProduct);

                // Send product to Product Service using Kafka
                productProducer.send(savedProduct, approvalRequest.getRequester(), ActionType.STATUS_UPDATE);
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private Product getProduct(ApprovalRequest approvalRequest) {
        return objectMapper.convertValue(approvalRequest.getData(), Product.class);
    }

    private ApprovalRequest getApprovalRequest(Product product, ActionType actionType) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setRequester(SecurityContextHolder.getContext().getAuthentication().getName());
        approvalRequest.setTopic(productApprovalTopic);
        approvalRequest.setTitle("Product Approval");
        approvalRequest.setData(product);
        approvalRequest.setActionType(actionType);
        approvalRequest.setApprovers(List.of(GlobalData.PRODUCT_MANAGER_ID, GlobalData.SELLER_MANAGER_ID, GlobalData.ADMIN_ID));
        return approvalRequest;
    }

}
