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
public class InitializeUserNotifications {
    private final SettingRepository settingRepository;

    @Autowired
    public InitializeUserNotifications(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public void initialize() {
        // customer-registration
        Setting register = new Setting();
        register.setRegistryKey("customer-registration");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the Customer after a successful registration");
        register.setUserTypes(List.of(UserType.CUSTOMER));
        EmailRegister emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/user-management/customer-registration");
        emailRegister.setSubject("Welcome to a new world of ECommerce!!!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // employee-approved
        register = new Setting();
        register.setRegistryKey("employee-approved");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the Employee after a successful registration and approval");
        register.setUserTypes(List.of(UserType.EMPLOYEE));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/user-management/employee-approved");
        emailRegister.setSubject("Welcome OnBoard!!!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // employee-registration
        register = new Setting();
        register.setRegistryKey("employee-registration");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the Employee after a successful registration and sent for the approval");
        register.setUserTypes(List.of(UserType.EMPLOYEE));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/user-management/employee-registration");
        emailRegister.setSubject("Two Steps To OnBoard! Employment In Approval!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // employment-details
        register = new Setting();
        register.setRegistryKey("employee-details");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the Employee about the update to the Employment Details");
        register.setUserTypes(List.of(UserType.EMPLOYEE));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/user-management/employee-details");
        emailRegister.setSubject("One Step to OnBoard! Employment Details got updated!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // seller-address
        register = new Setting();
        register.setRegistryKey("seller-address");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the Seller about the update to the address got accepted and sent for the approval");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/user-management/seller-address");
        emailRegister.setSubject("Update to Address In Approval!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // seller-approved
        register = new Setting();
        register.setRegistryKey("seller-approved");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the seller about successful registration and approval");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/user-management/seller-approved");
        emailRegister.setSubject("Welcome to the New World of Business!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // seller-registration
        register = new Setting();
        register.setRegistryKey("customer-registration");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the seller after a successful registration and sent for the approval");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/user-management/seller-registration");
        emailRegister.setSubject("One Step to Start the Business! Your Details In Approval!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // user-state
        register = new Setting();
        register.setRegistryKey("user-state");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the user about the update in the status");
        register.setUserTypes(List.of(UserType.CUSTOMER, UserType.EMPLOYEE, UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/user-management/user-state");
        emailRegister.setSubject("Account updated to ${status}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // user-update
        register = new Setting();
        register.setRegistryKey("user-update");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the user about the update to the account details sent for the approval");
        register.setUserTypes(List.of(UserType.SELLER, UserType.EMPLOYEE));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/user-management/user-update");
        emailRegister.setSubject("Account Update In Approval");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);
    }
}
