package com.academy.projects.ecommerce.productonboardingservice.dtos;

import com.academy.projects.ecommerce.productonboardingservice.models.ApprovalStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ApprovalRequest implements Serializable {
    private String id;
    private String requester;
    private List<String> approvers;
    private String assignedTo;
    private ApprovalStatus status;
    private String title;
    private Object data;
    private List<Action> actions;
    private String topic;
    private ActionType actionType;
}
