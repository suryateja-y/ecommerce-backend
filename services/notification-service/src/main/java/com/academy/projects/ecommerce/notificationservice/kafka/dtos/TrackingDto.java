package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

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
    private String customerId;
    private String trackingNumber;
    private TrackingStatus trackingStatus;
}
