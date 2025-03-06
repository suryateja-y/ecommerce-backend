package com.academy.projects.trackingmanagementservice.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Document(collection = "packages")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class OrderPackage extends BaseModel implements Serializable {
    private String orderId;
    private String trackingNumber;
    private Address deliveryAddress;
    private Address sellerAddress;
    private Date eta;
    private TrackingStatus trackingStatus;
    private List<TrackingAction> actions = new LinkedList<>();
}
