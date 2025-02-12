package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.models.EmailRegister;
import com.academy.projects.ecommerce.notificationservice.models.Notification;
import com.academy.projects.ecommerce.notificationservice.models.NotificationType;
import com.academy.projects.ecommerce.notificationservice.repositories.NotificationRepository;
import com.academy.projects.ecommerce.notificationservice.services.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationArchiveService implements INotificationArchiveService {
    private final NotificationRepository notificationRepository;
    private final IUserService userService;

    @Autowired
    public NotificationArchiveService(final NotificationRepository notificationRepository, final IUserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    @Override
    public void saveEmailNotification(EmailRegister emailRegister, String email, String subject, String htmlContent) {
        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.EMAIL);
        notification.setUserId(userService.getUserId(email));
        notification.setCreatedAt(new Date());
        notification.setCreatedBy("SYSTEM");
        notification.setData(prepareData(emailRegister, email, subject, htmlContent));
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> get(String userId, NotificationType notificationType, Date startDate, Date endDate, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Notification> notificationPage;
        if(userId != null) {
            if(notificationType != null) {
                if((startDate != null) && (endDate != null)) {
                    notificationPage = notificationRepository.findAllByUserIdAndNotificationTypeAndSentDateBetween(userId, notificationType, startDate, endDate, pageable);
                } else {
                    notificationPage = notificationRepository.findAllByUserIdAndNotificationType(userId, notificationType, pageable);
                }
            } else {
                if ((startDate != null) && (endDate != null)) {
                    notificationPage = notificationRepository.findAllByUserIdAndSentDateBetween(userId, startDate, endDate, pageable);
                } else {
                    notificationPage = notificationRepository.findAllByUserId(userId, pageable);
                }
            }
        } else {
            if(notificationType != null) {
                if((startDate != null) && (endDate != null)) {
                    notificationPage = notificationRepository.findAllByNotificationTypeAndSentDateBetween(notificationType, startDate, endDate, pageable);
                } else {
                    notificationPage = notificationRepository.findAllByNotificationType(notificationType, pageable);
                }
            } else {
                if ((startDate != null) && (endDate != null)) {
                    notificationPage = notificationRepository.findAllBySentDateBetween(startDate, endDate, pageable);
                } else {
                    notificationPage = notificationRepository.findAll(pageable);
                }
            }
        }
        return notificationPage.getContent();
    }

    private Object prepareData(EmailRegister emailRegister, String email, String subject, String htmlContent) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("subject", subject);
        data.put("body", htmlContent);
        data.put("recipient", email);
        data.put("bcc", emailRegister.getBcc());
        data.put("cc", emailRegister.getCc());
        return data;
    }
}
