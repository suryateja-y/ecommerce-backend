package com.academy.projects.ecommerce.notificationservice.controllers;

import com.academy.projects.ecommerce.notificationservice.models.Notification;
import com.academy.projects.ecommerce.notificationservice.models.NotificationType;
import com.academy.projects.ecommerce.notificationservice.models.Setting;
import com.academy.projects.ecommerce.notificationservice.models.UserType;
import com.academy.projects.ecommerce.notificationservice.services.notifications.INotificationArchiveService;
import com.academy.projects.ecommerce.notificationservice.services.notifications.ISettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/notifications")
public class NotificationController {
    private final ISettingService settingService;
    private final INotificationArchiveService notificationArchiveService;

    @Autowired
    public NotificationController(ISettingService settingService, INotificationArchiveService notificationArchiveService) {
        this.settingService = settingService;
        this.notificationArchiveService = notificationArchiveService;
    }

    @GetMapping("/settings")
    @PreAuthorize("hasAnyRole('ROLE_NOTIFICATION_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Setting>> getAllNotificationSettings(@RequestParam(required = false, defaultValue = "true") boolean active, @RequestParam(required = false) NotificationType notificationType, @RequestParam(required = false) UserType userType, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<Setting> settings = settingService.get(active, notificationType, userType, page, pageSize);
        return ResponseEntity.ok(settings);
    }

    @GetMapping("/settings/{registryKey}")
    @PreAuthorize("hasAnyRole('ROLE_NOTIFICATION_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<Setting> getNotificationSettings(@PathVariable String registryKey) {
        Setting settings = settingService.get(registryKey);
        return ResponseEntity.ok(settings);
    }

    @PostMapping("/settings")
    @PreAuthorize("hasAnyRole('ROLE_NOTIFICATION_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<Setting> createNotificationSettings(@RequestBody @Valid Setting register) {
        Setting settings = settingService.add(register);
        return new ResponseEntity<>(settings, HttpStatus.CREATED);
    }

    @PatchMapping("/settings/{registryKey}")
    @PreAuthorize("hasAnyRole('ROLE_NOTIFICATION_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<Setting> updateNotificationSettings(@PathVariable String registryKey, @RequestBody Setting register) {
        Setting settings = settingService.update(registryKey, register);
        return new ResponseEntity<>(settings, HttpStatus.ACCEPTED);
    }

    @GetMapping("/archive")
    @PreAuthorize("hasAnyRole('ROLE_NOTIFICATION_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<Notification>> getArchiveNotifications(@RequestParam(required = false) String userId, @RequestParam(required = false) NotificationType notificationType, @RequestParam(required = false) Date startDate, @RequestParam(required = false) Date endDate, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<Notification> notifications = notificationArchiveService.get(userId, notificationType, startDate, endDate, page, pageSize);
        return ResponseEntity.ok(notifications);
    }

}
