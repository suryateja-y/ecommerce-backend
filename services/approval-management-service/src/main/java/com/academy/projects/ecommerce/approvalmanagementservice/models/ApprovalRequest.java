package com.academy.projects.ecommerce.approvalmanagementservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "approvals")
@Getter
@Setter
@RequiredArgsConstructor
public class ApprovalRequest extends BaseModel {
    @NotBlank(message = "Requester data should present")
    private String requester;

    @NotEmpty(message = "Provide approvers list")
    private List<String> approvers;

    @NotBlank(message = "Assign to should not be blank")
    private String assignedTo;

    private String previousAssignedTo;

    private ApprovalStatus status;

    @NotBlank(message = "Provide the title for the approval request")
    private String title;

    @NotEmpty(message = "Provide details")
    private Object data;
    private List<Action> actions;

//    @JsonIgnore
    @NotBlank(message = "Request approval Kafka topic is mandatory to send the update")
    private String topic;

    @NotBlank(message = "Action Type should be specified!!!")
    private ActionType actionType;

}
