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
public class InitializePaymentNotifications {
    private final SettingRepository settingRepository;

    @Autowired
    public InitializePaymentNotifications(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public void initialize() {
        // payment-initiate
        Setting register = new Setting();
        register.setRegistryKey("payment-initiate");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification Send to the Customer to inform Payment got initiated for the specific order");
        register.setUserTypes(List.of(UserType.CUSTOMER));
        EmailRegister emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/payment/payment-initiate");
        emailRegister.setSubject("Payment Initiation - ${orderId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // payment-refund
        register = new Setting();
        register.setRegistryKey("payment-refund");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification Send to the Customer to inform refund request got initiated for the specific order");
        register.setUserTypes(List.of(UserType.CUSTOMER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/payment/payment-refund");
        emailRegister.setSubject("Payment Refund - ${orderId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // payment-status
        register = new Setting();
        register.setRegistryKey("payment-status");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the Customer to inform about any status update related to the payment");
        register.setUserTypes(List.of(UserType.CUSTOMER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/payment/payment-status");
        emailRegister.setSubject("Attention!!! Payment Status Update - ${orderId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // payment-success
        register = new Setting();
        register.setRegistryKey("payment-success");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the Customer to inform the Payment is successful");
        register.setUserTypes(List.of(UserType.CUSTOMER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/payment/payment-success");
        emailRegister.setSubject("Payment Successful - ${orderId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);
    }
}
