package com.academy.projects.trackingmanagementservice.exceptions;

import com.academy.projects.trackingmanagementservice.models.PackageStatus;
import lombok.ToString;

@ToString
public class PackageAlreadyPackedOrCancelledException extends RuntimeException {
    public PackageAlreadyPackedOrCancelledException(String packageRequestId, PackageStatus packageStatus) {
        super("Package '" + packageRequestId + "' is already '" + packageStatus + "'.!!!");
    }
}
