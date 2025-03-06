package com.academy.projects.trackingmanagementservice.services;

import com.academy.projects.trackingmanagementservice.dtos.CancelResponseDto;
import com.academy.projects.trackingmanagementservice.models.OrderPackage;
import com.academy.projects.trackingmanagementservice.models.PackageRequest;
import com.academy.projects.trackingmanagementservice.models.TrackingStatus;

import java.util.Date;
import java.util.List;

public interface IPackageService {
    CancelResponseDto cancel(String orderId);
    OrderPackage convert(PackageRequest packageRequest);
    List<OrderPackage> get(String trackingNumber, String orderId, TrackingStatus trackingStatus, Date from, Date to, int page, int pageSize);
    OrderPackage get(String trackingNumber, String name);
    OrderPackage update(String trackingNumber, String destination, TrackingStatus trackingStatus, String message);
}
