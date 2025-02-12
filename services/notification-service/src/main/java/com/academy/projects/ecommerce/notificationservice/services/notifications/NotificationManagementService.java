package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.models.NotificationType;
import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationManagementService implements INotificationManagementService {
    private final NotificationServiceFactory notificationServiceFactory;
    private final ISettingService settingService;

    @Autowired
    public NotificationManagementService(NotificationServiceFactory notificationServiceFactory, ISettingService registerService) {
        this.notificationServiceFactory = notificationServiceFactory;
        this.settingService = registerService;
    }

    @Override
    public void send(String registerKey, Map<String, Object> data, RecipientCommunicationDetails recipientDetails) {
        Setting register = settingService.get(registerKey);
        for(NotificationType notificationType : register.getNotificationTypes()) {
            notificationServiceFactory.getNotificationService(notificationType).send(register, data, recipientDetails);
        }
    }
}
