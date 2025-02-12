package com.academy.projects.ecommerce.approvalmanagementservice.services;

import com.academy.projects.ecommerce.approvalmanagementservice.dtos.UpdateStatusRequestDto;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalStatus;

import java.util.List;

public interface IApprovalService {
    ApprovalRequest register(ApprovalRequest approvalRequest);
    ApprovalRequest update(ApprovalRequest approvalRequest, String requester);
    ApprovalRequest updateStatus(UpdateStatusRequestDto requestDto, String approvalId, String requester);
    ApprovalRequest getApprovalRequest(String approvalId, String requester);
    ApprovalRequest getApprovalRequest(String approvalId);
    List<ApprovalRequest> getApprovalRequestsOfUser(String requester, int page, int pageSize, ApprovalStatus approvalStatus);
    List<ApprovalRequest> getApprovalRequests(int page, int pageSize, ApprovalStatus approvalStatus);
    ApprovalRequest updateStatus(String approvalId, ApprovalStatus approvalStatus, String requesterId);
}
