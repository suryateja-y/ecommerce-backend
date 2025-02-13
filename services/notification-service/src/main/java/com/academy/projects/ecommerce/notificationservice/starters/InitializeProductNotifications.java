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
public class InitializeProductNotifications {
    private final SettingRepository settingRepository;
    private final StarterData starterData;

    @Autowired
    public InitializeProductNotifications(SettingRepository settingRepository, StarterData starterData) {
        this.settingRepository = settingRepository;
        this.starterData = starterData;
    }

    public void initialize() {
        // product-approved
        Setting register = new Setting();
        register.setRegistryKey("product-approved");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the requester of the product to inform the product has been approved");
        register.setUserTypes(List.of(UserType.SELLER));
        EmailRegister emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/product/product-approved");
        emailRegister.setCc(List.of(starterData.getProductManagerId()));
        emailRegister.setSubject("Get ready to expand the business! Your product has been approved!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // product-approved-sellers
        register = new Setting();
        register.setRegistryKey("product-approved-sellers");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to all the sellers informing a new product has been added");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/product/product-approved-sellers");
        emailRegister.setSubject("Get ready to expand the business! A New Product has been added!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // product-create
        register = new Setting();
        register.setRegistryKey("product-create");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification to inform the requester of the product that the requested product is accepted and send for the approval");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/product/product-create");
        emailRegister.setSubject("Your Product got accepted and In Approval - ${productId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // product-delete
        register = new Setting();
        register.setRegistryKey("product-delete");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification to inform all the sellers that the product has been deactivated and not for sale");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/product/product-delete");
        emailRegister.setSubject("Product is discontinued - ${productId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // product-status
        register = new Setting();
        register.setRegistryKey("product-status");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the requester on the status update of the product");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/product/product-status");
        emailRegister.setSubject("Update to Your Product - ${productId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // product-update
        register = new Setting();
        register.setRegistryKey("product-update");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification to inform the requester about the update on the product has been accepted and send for the approval");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/product/product-update");
        emailRegister.setSubject("Your Product Update got accepted and In Approval - ${productId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);
    }
}
