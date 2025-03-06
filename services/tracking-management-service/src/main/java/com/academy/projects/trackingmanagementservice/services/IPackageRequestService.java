package com.academy.projects.trackingmanagementservice.services;

import com.academy.projects.trackingmanagementservice.dtos.CancelResponseDto;
import com.academy.projects.trackingmanagementservice.models.OrderPackage;
import com.academy.projects.trackingmanagementservice.models.PackageRequest;
import com.academy.projects.trackingmanagementservice.models.PackageStatus;

import java.util.List;

public interface IPackageRequestService {
    void create(PackageRequest from);
    CancelResponseDto cancel(String orderId);
    List<PackageRequest> get(String packageRequestId, String orderId, String customerId, String sellerId, PackageStatus packageStatus, int page, int pageSize);
    OrderPackage convert(String packageRequestId);
    PackageRequest get(String packageRequestId, String userId);
}
