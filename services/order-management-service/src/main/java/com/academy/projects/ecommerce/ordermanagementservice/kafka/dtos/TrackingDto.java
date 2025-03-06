package com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos;

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
}
