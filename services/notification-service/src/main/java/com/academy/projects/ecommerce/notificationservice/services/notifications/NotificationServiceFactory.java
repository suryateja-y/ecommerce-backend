package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.models.NotificationType;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceFactory {

    private final IEmailNotificationService emailNotificationService;
    private final ISMSNotificationService smsNotificationService;
    private final IPushNotificationService pushNotificationService;

    public NotificationServiceFactory(IEmailNotificationService emailNotificationService, ISMSNotificationService smsNotificationService, IPushNotificationService pushNotificationService) {
        this.emailNotificationService = emailNotificationService;
        this.smsNotificationService = smsNotificationService;
        this.pushNotificationService = pushNotificationService;
    }
    public INotificationService getNotificationService(NotificationType notificationType) {
        return switch (notificationType) {
            case EMAIL -> emailNotificationService;
            case SMS -> smsNotificationService;
            case PUSH -> pushNotificationService;
        };
    }
}
