package com.academy.projects.ecommerce.notificationservice.exceptions;

import lombok.ToString;

@ToString
public class NotificationSettingsAlreadyExistsException extends RuntimeException {
    public NotificationSettingsAlreadyExistsException(String registryKey) {
        super("Notification settings with key '" + registryKey + "' already exists!!!");
    }
}
