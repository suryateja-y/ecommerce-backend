package com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.productonboardingservice.models.Product;

public interface IProductApprovalManager {
    String register(Product product, ActionType actionType);
    void updateStatus(ApprovalRequest approvalRequest);
}
