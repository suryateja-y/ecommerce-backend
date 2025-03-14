package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

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
    private String customerId;
    private String trackingNumber;
    private TrackingStatus trackingStatus;
}
