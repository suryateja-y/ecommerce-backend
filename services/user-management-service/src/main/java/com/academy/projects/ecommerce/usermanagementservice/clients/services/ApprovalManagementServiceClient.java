package com.academy.projects.ecommerce.usermanagementservice.clients.services;

import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.ApprovalResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.dtos.ApprovalRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${application.services.approval-management-service}")
public interface ApprovalManagementServiceClient {
    @PostMapping("/api/${application.version}/approvals/register")
    ApprovalResponseDto registerUserForApproval(@RequestBody ApprovalRequest approvalRequest);
}
