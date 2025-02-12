package com.academy.projects.ecommerce.approvalmanagementservice.exceptions;

import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalStatus;
import lombok.ToString;

@ToString
public class NoAccessToChangeApprovalRequestException extends RuntimeException {
    public NoAccessToChangeApprovalRequestException(String requester) {
        super("Approval Request is on somebody else's name!!! '" + requester + "' does not have access to update the Approval Request!!!");
    }
    public NoAccessToChangeApprovalRequestException(ApprovalStatus approvalStatus) {
        super("Approval Status: '" + approvalStatus + "' is not valid for direct update!!!");
    }
}
