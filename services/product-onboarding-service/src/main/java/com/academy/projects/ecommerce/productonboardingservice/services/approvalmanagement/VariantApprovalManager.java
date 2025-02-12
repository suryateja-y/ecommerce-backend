package com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement;

import com.academy.projects.ecommerce.productonboardingservice.starters.GlobalData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.academy.projects.ecommerce.productonboardingservice.clients.dtos.ApprovalResponseDto;
import com.academy.projects.ecommerce.productonboardingservice.clients.dtos.ResponseStatus;
import com.academy.projects.ecommerce.productonboardingservice.clients.services.ApprovalManagementServiceClient;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.productonboardingservice.exceptions.VariantApprovalRegistrationException;
import com.academy.projects.ecommerce.productonboardingservice.kafka.producer.services.VariantProducer;
import com.academy.projects.ecommerce.productonboardingservice.models.Variant;
import com.academy.projects.ecommerce.productonboardingservice.repositories.VariantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariantApprovalManager implements IVariantApprovalManager {

    @Value("${application.kafka.topics.variant-approval-topic}")
    private String variantApprovalTopic;

    private final ApprovalManagementServiceClient approvalManagementServiceClient;
    private final VariantRepository variantRepository;
    private final VariantProducer variantProducer;
    private final ObjectMapper objectMapper;

    public VariantApprovalManager(final ApprovalManagementServiceClient approvalManagementServiceClient, final VariantProducer variantProducer, final VariantRepository variantRepository, final ObjectMapper objectMapper) {
        this.approvalManagementServiceClient = approvalManagementServiceClient;
        this.variantProducer = variantProducer;
        this.variantRepository = variantRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public String register(Variant variant, ActionType actionType) {
        try {
            ApprovalRequest approvalRequest = getApprovalRequest(variant, actionType);
            ApprovalResponseDto responseDto = approvalManagementServiceClient.registerForApproval(approvalRequest);
            if(responseDto.getResponseStatus() == ResponseStatus.SUCCESS) {
                variant.setApprovalId(responseDto.getApprovalId());
                variantProducer.send(variant, approvalRequest.getRequester(), actionType);
                return responseDto.getApprovalId();
            }
            else throw new VariantApprovalRegistrationException(variant.getId());
        } catch(Exception e) {
            throw new VariantApprovalRegistrationException(variant.getId());
        }
    }

    @Override
    public void updateStatus(ApprovalRequest approvalRequest) {
        Variant variant = this.getVariant(approvalRequest);
        Variant savedVariant = variantRepository.findById(variant.getId()).orElse(null);
        if(savedVariant != null) {
            savedVariant.setApprovalStatus(approvalRequest.getStatus());
            variantRepository.save(savedVariant);

            // Send variant to Product Service using Kafka
            variantProducer.send(savedVariant, approvalRequest.getRequester(), ActionType.STATUS_UPDATE);
        }
    }

    private Variant getVariant(ApprovalRequest approvalRequest) {
        return objectMapper.convertValue(approvalRequest.getData(), Variant.class);
    }

    private ApprovalRequest getApprovalRequest(Variant variant, ActionType actionType) {
        ApprovalRequest approvalRequest = new ApprovalRequest();
        approvalRequest.setRequester(SecurityContextHolder.getContext().getAuthentication().getName());
        approvalRequest.setTopic(variantApprovalTopic);
        approvalRequest.setTitle("Variant Approval");
        approvalRequest.setData(variant);
        approvalRequest.setActionType(actionType);
        approvalRequest.setApprovers(List.of(GlobalData.PRODUCT_MANAGER_ID, GlobalData.SELLER_MANAGER_ID, GlobalData.ADMIN_ID));
        return approvalRequest;
    }
}
