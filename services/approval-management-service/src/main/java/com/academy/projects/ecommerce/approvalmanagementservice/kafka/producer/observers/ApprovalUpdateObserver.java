package com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.observers;

import com.academy.projects.ecommerce.approvalmanagementservice.kafka.dtos.UpdateType;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;

public interface ApprovalUpdateObserver {
    void sendUpdate(ApprovalRequest request, UpdateType updateType);
}
