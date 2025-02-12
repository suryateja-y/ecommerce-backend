package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.models.NotificationType;
import com.academy.projects.ecommerce.notificationservice.models.Setting;
import com.academy.projects.ecommerce.notificationservice.models.UserType;

import java.util.List;

public interface ISettingService {
    Setting get(String registerKey);
    List<Setting> get(boolean active, NotificationType notificationType, UserType userType, int page, int pageSize);
    Setting add(Setting setting);
    Setting update(String registryKey, Setting setting);
}
