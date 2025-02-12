package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalNotificationDto implements Serializable {
    private String requestId;
    private String title;
    private ApprovalStatus approvalStatus;
    private String previousAssignee;
    private String currentAssignee;
    private String requester;
    private Action updateType;
}
