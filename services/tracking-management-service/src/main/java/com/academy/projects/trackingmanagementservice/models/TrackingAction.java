package com.academy.projects.trackingmanagementservice.models;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
public class TrackingAction implements Serializable {
    private String currentLocation;
    private String destination;
    private TrackingStatus trackingStatus;
    private String message;
    private Date actedOn;
}
