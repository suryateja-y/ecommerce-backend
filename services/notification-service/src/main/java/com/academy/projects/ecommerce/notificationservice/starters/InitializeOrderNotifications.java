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
public class InitializeOrderNotifications {
    private final SettingRepository settingRepository;

    @Autowired
    public InitializeOrderNotifications(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public void initialize() {
        // order-create
        Setting register = new Setting();
        register.setRegistryKey("order-create");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification Send to the Customer to inform Order got place");
        register.setUserTypes(List.of(UserType.CUSTOMER));
        EmailRegister emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/order/order-create");
        emailRegister.setSubject("Hurray!!! Order got placed - ${orderId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // order-status
        register = new Setting();
        register.setRegistryKey("order-status");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification Send to the Customer about any update in the process of placing the order");
        register.setUserTypes(List.of(UserType.CUSTOMER));
        EmailRegister emailRegister2 = new EmailRegister();
        emailRegister2.setTemplateName("/email/order/order-status");
        emailRegister2.setSubject("Attention!!! Update to the Order - ${orderId}");
        register.setEmailRegister(emailRegister2);
        settingRepository.save(register);
    }
}
