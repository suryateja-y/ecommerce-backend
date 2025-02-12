package com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.publishers;

import com.academy.projects.ecommerce.approvalmanagementservice.kafka.dtos.UpdateType;
import com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.observers.ApprovalUpdateObserver;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ApprovalUpdatePublisher implements IApprovalUpdatePublisher {
    private final List<ApprovalUpdateObserver> observers = new LinkedList<>();

    @Override
    public void addObserver(final ApprovalUpdateObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(final ApprovalUpdateObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(ApprovalRequest request, UpdateType updateType) {
        for (ApprovalUpdateObserver observer : observers) {
            observer.sendUpdate(request, updateType);
        }
    }
}
