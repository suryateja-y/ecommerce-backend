package com.academy.projects.ecommerce.usermanagementservice.models;

import lombok.Getter;

@Getter
public enum UserState {
    PENDING_FOR_APPROVAL("PENDING_FOR_APPROVAL"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    IN_ACTIVE("IN_ACTIVE"),
    EMPTY("EMPTY");

    public final String userState;

    UserState(String approvalStatus) {
        this.userState = approvalStatus;
    }
}
