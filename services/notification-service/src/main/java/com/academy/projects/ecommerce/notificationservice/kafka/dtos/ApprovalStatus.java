package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.Getter;

@Getter
public enum ApprovalStatus {
    PENDING_FOR_APPROVAL("PENDING_FOR_APPROVAL"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    NEED_MORE_INFORMATION("NEED_MORE_INFORMATION"),
    NEED_FURTHER_APPROVAL("NEED_FURTHER_APPROVAL"),
    CANCELLED("CANCELLED");

    public final String approvalStatus;

    ApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

}
