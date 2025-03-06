package com.academy.projects.trackingmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class PackageRequestNotFoundException extends RuntimeException {
    public PackageRequestNotFoundException(String packageRequestId) {
        super("Package Request '" + packageRequestId + "' not found!!!");
    }
    public PackageRequestNotFoundException(String packageRequestId, String userId) {
        super("Package Request '" + packageRequestId + "' not found for customer / seller '" + userId + "'!!!");
    }
}
