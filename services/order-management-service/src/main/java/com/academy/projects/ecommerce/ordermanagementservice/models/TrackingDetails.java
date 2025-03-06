package com.academy.projects.ecommerce.ordermanagementservice.models;

import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.TrackingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tracking_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingDetails {
    @Id
    private String trackingId;
    private String trackingNumber;
    private TrackingStatus trackingStatus;
    private String comment;
}
