package com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.productonboardingservice.models.Variant;

public interface IVariantApprovalManager {
    String register(Variant variant, ActionType actionType);
    void updateStatus(ApprovalRequest approvalRequest);
}
