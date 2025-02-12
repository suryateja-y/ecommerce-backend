package com.academy.projects.ecommerce.productonboardingservice.services.approvalmanagement;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.productonboardingservice.models.InventoryUnit;

public interface IInventoryApprovalManager {
    String register(InventoryUnit inventoryUnit, ActionType actionType);
    void updateStatus(ApprovalRequest approvalRequest);
}
