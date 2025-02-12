package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.models.EmailRegister;
import com.academy.projects.ecommerce.notificationservice.models.Notification;
import com.academy.projects.ecommerce.notificationservice.models.NotificationType;

import java.util.Date;
import java.util.List;

public interface INotificationArchiveService {
    void saveEmailNotification(EmailRegister emailRegister, String email, String subject, String htmlContent);
    List<Notification> get(String userId, NotificationType notificationType, Date startDate, Date endDate, int page, int pageSize);
}
