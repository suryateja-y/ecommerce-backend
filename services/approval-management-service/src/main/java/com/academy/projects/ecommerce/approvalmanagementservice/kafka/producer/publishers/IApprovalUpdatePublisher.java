package com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.publishers;

import com.academy.projects.ecommerce.approvalmanagementservice.kafka.dtos.UpdateType;
import com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.observers.ApprovalUpdateObserver;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;

public interface IApprovalUpdatePublisher {
    void addObserver(ApprovalUpdateObserver observer);
    void notify(ApprovalRequest request, UpdateType updateType);
}
