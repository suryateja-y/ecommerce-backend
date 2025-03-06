package com.academy.projects.trackingmanagementservice.kafka.dtos;

import com.academy.projects.trackingmanagementservice.models.TrackingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class ActionDto implements Serializable {
    private String destination;
    private TrackingStatus trackingStatus;
    private String message;
}
