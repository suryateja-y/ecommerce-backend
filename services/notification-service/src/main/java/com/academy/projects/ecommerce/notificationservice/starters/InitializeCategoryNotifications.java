package com.academy.projects.ecommerce.notificationservice.starters;

import com.academy.projects.ecommerce.notificationservice.models.EmailRegister;
import com.academy.projects.ecommerce.notificationservice.models.NotificationType;
import com.academy.projects.ecommerce.notificationservice.models.Setting;
import com.academy.projects.ecommerce.notificationservice.models.UserType;
import com.academy.projects.ecommerce.notificationservice.repositories.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitializeCategoryNotifications {
    private final SettingRepository settingRepository;

    @Autowired
    public InitializeCategoryNotifications(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public void initialize() {
        // category-create
        Setting register = new Setting();
        register.setRegistryKey("category-create");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to all the sellers to inform a new category has been added into the application");
        register.setUserTypes(List.of(UserType.SELLER));
        EmailRegister emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/category/category-create");
        emailRegister.setSubject("Expand Yourself - New Category Added");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);


        // category-update
        register = new Setting();
        register.setRegistryKey("category-update");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to all the sellers to inform about the update to the category");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/category/category-update");
        emailRegister.setSubject("Attention!!! Category Update - ${categoryId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // category-delete
        register = new Setting();
        register.setRegistryKey("category-delete");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to all the sellers to inform about the deletion of the category");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/category/category-delete");
        emailRegister.setSubject("Attention!!! Category Delete - ${categoryId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

    }
}
