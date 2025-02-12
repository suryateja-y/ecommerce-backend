package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.configurations.Patcher;
import com.academy.projects.ecommerce.notificationservice.exceptions.NotificationSettingsAlreadyExistsException;
import com.academy.projects.ecommerce.notificationservice.exceptions.NotificationSettingsNotFoundException;
import com.academy.projects.ecommerce.notificationservice.exceptions.UserNotFoundException;
import com.academy.projects.ecommerce.notificationservice.models.NotificationType;
import com.academy.projects.ecommerce.notificationservice.models.Setting;
import com.academy.projects.ecommerce.notificationservice.models.UserType;
import com.academy.projects.ecommerce.notificationservice.repositories.SettingRepository;
import com.academy.projects.ecommerce.notificationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService implements ISettingService {
    private final SettingRepository settingRepository;
    private final UserRepository userRepository;
    private final Patcher patcher;

    @Autowired
    public SettingService(SettingRepository settingRepository, UserRepository userRepository, Patcher patcher) {
        this.settingRepository = settingRepository;
        this.userRepository = userRepository;
        this.patcher = patcher;
    }

    @Override
    public Setting get(String registerKey) {
        return settingRepository.findById(registerKey).orElseThrow(() -> new NotificationSettingsNotFoundException(registerKey));
    }

    @Override
    public List<Setting> get(boolean active, NotificationType notificationType, UserType userType, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Setting> settings;
        if(notificationType != null) {
            if(userType != null) {
                settings = settingRepository.findAllByActiveAndNotificationTypesContainsAndUserTypesContains(active, notificationType, userType, pageable);
            } else {
                settings = settingRepository.findAllByActiveAndNotificationTypesContains(active, notificationType, pageable);
            }
        } else {
            if(userType != null) {
                settings = settingRepository.findAllByActiveAndUserTypesContains(active, userType, pageable);
            } else {
                settings = settingRepository.findAllByActive(active, pageable);
            }
        }
        return settings.getContent();
    }

    @Override
    public Setting add(Setting setting) {
        if(settingRepository.existsByRegistryKey(setting.getRegistryKey())) throw new NotificationSettingsAlreadyExistsException(setting.getRegistryKey());
        validate(setting.getEmailRegister().getCc());
        validate(setting.getEmailRegister().getBcc());
        return settingRepository.save(setting);
    }

    @Override
    public Setting update(String registryKey, Setting setting) {
        Setting savedSetting = settingRepository.findByRegistryKey(registryKey).orElseThrow(() -> new NotificationSettingsNotFoundException(registryKey));
        if((setting.getEmailRegister() != null) && (setting.getEmailRegister().getCc() != null))
            validate(setting.getEmailRegister().getCc());
        if((setting.getEmailRegister() != null) && (setting.getEmailRegister().getBcc() != null))
            validate(setting.getEmailRegister().getBcc());
        patcher.entity(savedSetting, setting, Setting.class);
        return settingRepository.save(savedSetting);
    }

    private void validate(List<String> users) {
        for(String user : users) {
            if(!userRepository.existsByUserId(user)) throw new UserNotFoundException(user);
        }
    }
}
