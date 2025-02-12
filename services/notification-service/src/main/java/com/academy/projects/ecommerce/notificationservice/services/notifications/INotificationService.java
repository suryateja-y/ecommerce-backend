package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.Setting;

import java.util.Map;

public interface INotificationService {
    void send(Setting register, Map<String, Object> data, RecipientCommunicationDetails communicationDetails);
}
