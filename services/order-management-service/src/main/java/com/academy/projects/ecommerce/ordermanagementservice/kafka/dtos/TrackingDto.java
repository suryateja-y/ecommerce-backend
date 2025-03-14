package com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackingDto implements Serializable {
    private String orderId;
    private String trackingId;
    private String trackingNumber;
    private TrackingStatus trackingStatus;
    private Action action;
    private ActionStatus actionStatus;
    private String message;
}
