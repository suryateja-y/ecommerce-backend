package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;

import java.util.Map;

public interface INotificationManagementService {
    void send(String registerKey, Map<String, Object> data, RecipientCommunicationDetails communicationDetails);
}
