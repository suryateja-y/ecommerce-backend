package com.academy.projects.trackingmanagementservice.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "package_requests")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class PackageRequest extends BaseModel implements Serializable {
    private String orderId;
    private String deliveryAddressId;
    private String sellerId;
    private String customerId;
    private Date eta;
    private PackageStatus packageStatus;

    @Reference
    private OrderPackage orderPackage;
}
