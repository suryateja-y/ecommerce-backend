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
public class InitializeInventoryNotifications {
    private final SettingRepository settingRepository;

    @Autowired
    public InitializeInventoryNotifications(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public void initialize() {
        // inventory-approved
        Setting register = new Setting();
        register.setRegistryKey("inventory-approved");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the seller when the inventory got approved");
        register.setUserTypes(List.of(UserType.SELLER));
        EmailRegister emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/inventory/inventory-approved");
        emailRegister.setSubject("Inventory APPROVED - ${inventoryId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // inventory-create
        register = new Setting();
        register.setRegistryKey("inventory-create");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the seller when the Inventory Create request has been created and sent for the approval.");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/inventory/inventory-create");
        emailRegister.setSubject("Inventory Request Created - ${inventoryId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // inventory-status
        register = new Setting();
        register.setRegistryKey("inventory-status");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the seller if there is any update to the Inventory Request");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/inventory/inventory-status");
        emailRegister.setSubject("Update to your Inventory Request - ${inventoryId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // inventory-update
        register = new Setting();
        register.setRegistryKey("inventory-update");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the seller to confirm the update requested has been registered and sent for the approval.");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/inventory/inventory-update");
        emailRegister.setSubject("Inventory Update Request - ${inventoryId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // no-stock
        register = new Setting();
        register.setRegistryKey("no-stock");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the seller to inform stock got empty for a respective variant");
        register.setUserTypes(List.of(UserType.SELLER, UserType.EMPLOYEE));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/inventory/no-stock");
        emailRegister.setSubject("Attention!!! NO STOCK!!!");
        emailRegister.setBcc(List.of(GlobalData.PRODUCT_MANAGER_ID));
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);
    }
}
