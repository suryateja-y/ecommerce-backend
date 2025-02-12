package com.academy.projects.ecommerce.notificationservice.exceptions;

import lombok.ToString;

@ToString
public class NotificationSettingsNotFoundException extends RuntimeException {
    public NotificationSettingsNotFoundException(String registerKey) {
        super("Notification Settings with Registration Key " + registerKey + " not found!!!");
    }
}
