package com.academy.projects.ecommerce.approvalmanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalStatus;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto implements Serializable {
    private String requestId;
    private String title;
    private ApprovalStatus approvalStatus;
    private String previousAssignee;
    private String currentAssignee;
    private String requester;
    private UpdateType updateType;
}
