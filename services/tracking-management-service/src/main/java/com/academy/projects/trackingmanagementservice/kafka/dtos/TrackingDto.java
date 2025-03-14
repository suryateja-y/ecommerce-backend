package com.academy.projects.trackingmanagementservice.kafka.dtos;

import com.academy.projects.trackingmanagementservice.dtos.ActionStatus;
import com.academy.projects.trackingmanagementservice.models.TrackingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class TrackingDto implements Serializable {
    private String orderId;
    private String trackingId;
    private String trackingNumber;
    private TrackingStatus trackingStatus;
    private Action action;
    private ActionStatus actionStatus;
    private String message;
    private String customerId;
}
