package com.academy.projects.trackingmanagementservice.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "tracking_number_sequence")
@Getter
@Setter
@Builder
public class TrackingNumber {
    @MongoId
    @Id
    private String id;

    private Long trackingNumber;
}
