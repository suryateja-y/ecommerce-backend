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
public class InitializeTrackingNotifications {
    private final SettingRepository settingRepository;

    @Autowired
    public InitializeTrackingNotifications(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public void initialize() {
        // tracking - shipped
        Setting register = new Setting();
        register.setRegistryKey("tracking-shipped");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the customer to inform the shipment has been shipped");
        register.setUserTypes(List.of(UserType.CUSTOMER));
        EmailRegister emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/tracking/tracking-shipped");
        emailRegister.setSubject("Order - ${orderId} has been shipped!!!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // tracking - out - for - delivery
        register = new Setting();
        register.setRegistryKey("tracking-out-for-delivery");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the customer to inform the shipment is out for delivery");
        register.setUserTypes(List.of(UserType.CUSTOMER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/tracking/tracking-out-for-delivery");
        emailRegister.setSubject("Order - ${orderId} is Out for delivery!!!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // tracking - delivered
        register = new Setting();
        register.setRegistryKey("tracking-delivered");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the customer confirming the order shipment has been delivered!!!");
        register.setUserTypes(List.of(UserType.CUSTOMER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/tracking/tracking-delivered");
        emailRegister.setSubject("Order - ${orderId} has been delivered!!!}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);
    }
}
