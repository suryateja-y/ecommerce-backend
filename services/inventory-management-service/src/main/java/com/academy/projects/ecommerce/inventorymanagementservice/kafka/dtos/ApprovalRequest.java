package com.academy.projects.ecommerce.inventorymanagementservice.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
