package com.academy.projects.ecommerce.usermanagementservice.services.approvalmanagement;

import com.academy.projects.ecommerce.usermanagementservice.dtos.ApprovalRequest;

public interface IUserApprovalManager {
    void updateStatus(ApprovalRequest approvalRequest);
}
