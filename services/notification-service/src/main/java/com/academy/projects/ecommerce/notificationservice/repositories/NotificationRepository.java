package com.academy.projects.ecommerce.notificationservice.repositories;

import com.academy.projects.ecommerce.notificationservice.models.Notification;
import com.academy.projects.ecommerce.notificationservice.models.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    Page<Notification> findAllByUserIdAndNotificationTypeAndSentDateBetween(String userId, NotificationType notificationType, Date startDate, Date endDate, Pageable pageable);
    Page<Notification> findAllByUserIdAndNotificationType(String userId, NotificationType notificationType, Pageable pageable);
    Page<Notification> findAllByUserIdAndSentDateBetween(String userId, Date startDate, Date endDate, Pageable pageable);
    Page<Notification> findAllByUserId(String userId, Pageable pageable);
    Page<Notification> findAllByNotificationTypeAndSentDateBetween(NotificationType notificationType, Date startDate, Date endDate, Pageable pageable);
    Page<Notification> findAllByNotificationType(NotificationType notificationType, Pageable pageable);
    Page<Notification> findAllBySentDateBetween(Date startDate, Date endDate, Pageable pageable);
}
