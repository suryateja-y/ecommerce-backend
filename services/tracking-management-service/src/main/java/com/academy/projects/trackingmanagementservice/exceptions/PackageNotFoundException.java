package com.academy.projects.trackingmanagementservice.exceptions;

import lombok.ToString;

@ToString
public class PackageNotFoundException extends RuntimeException {
    public PackageNotFoundException(String trackingNumber) {
        super("Package '" + trackingNumber + "' not found!!!");
    }
}
