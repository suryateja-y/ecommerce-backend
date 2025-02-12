package com.academy.projects.ecommerce.productonboardingservice.kafka.consumer.services;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement.IInventoryApprovalManager;
import com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement.IProductApprovalManager;
import com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement.IVariantApprovalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ApprovalConsumer {
    private final IProductApprovalManager productApprovalManager;
    private final IVariantApprovalManager variantApprovalManager;
    private final IInventoryApprovalManager inventoryApprovalManager;

    @Autowired
    public ApprovalConsumer(IProductApprovalManager productApprovalManager, IVariantApprovalManager variantApprovalManager, IInventoryApprovalManager inventoryApprovalManager) {
        this.productApprovalManager = productApprovalManager;
        this.variantApprovalManager = variantApprovalManager;
        this.inventoryApprovalManager = inventoryApprovalManager;
    }

    @KafkaListener(topics = "${application.kafka.topics.product-approval-topic}", groupId = "${application.kafka.consumer.product-approval-group}", containerFactory = "kafkaListenerContainerFactory")
    public void consumer(ApprovalRequest approvalRequest) {
        try {
            productApprovalManager.updateStatus(approvalRequest);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.variant-approval-topic}", groupId = "${application.kafka.consumer.variant-approval-group}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeVariant(ApprovalRequest approvalRequest) {
        try {
            variantApprovalManager.updateStatus(approvalRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.inventory-approval-topic}", groupId = "${application.kafka.consumer.inventory-approval-group}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeInventory(ApprovalRequest approvalRequest) {
        try {
            inventoryApprovalManager.updateStatus(approvalRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
