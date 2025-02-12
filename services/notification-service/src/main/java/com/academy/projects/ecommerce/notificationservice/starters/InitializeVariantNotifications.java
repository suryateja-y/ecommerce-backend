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
public class InitializeVariantNotifications {
    private final SettingRepository settingRepository;

    @Autowired
    public InitializeVariantNotifications(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public void initialize() {
        // variant-approved
        Setting register = new Setting();
        register.setRegistryKey("variant-approved");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the requester of the variant to inform the variant has been approved");
        register.setUserTypes(List.of(UserType.SELLER));
        EmailRegister emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/variant/variant-approved");
        emailRegister.setCc(List.of(GlobalData.PRODUCT_MANAGER_ID));
        emailRegister.setSubject("Get ready to expand the business! Your variant has been approved!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // variant-approved-sellers
        register = new Setting();
        register.setRegistryKey("variant-approved-sellers");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to all the sellers informing a new variant has been added");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/variant/variant-approved-sellers");
        emailRegister.setSubject("Get ready to expand the business! A New Variant has been added!");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // variant-create
        register = new Setting();
        register.setRegistryKey("variant-create");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification to inform the requester of the variant that the requested variant is accepted and send for the approval");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/variant/variant-create");
        emailRegister.setSubject("Your Variant got accepted and In Approval - ${variantId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // variant-delete
        register = new Setting();
        register.setRegistryKey("variant-delete");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification to inform all the sellers that the variant has been deactivated and not for sale");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/variant/variant-delete");
        emailRegister.setSubject("Variant is discontinued - ${variantId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // variant-status
        register = new Setting();
        register.setRegistryKey("variant-status");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the requester on the status update of the variant");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/variant/variant-status");
        emailRegister.setSubject("Update to Your Variant - ${variantId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // variant-update
        register = new Setting();
        register.setRegistryKey("variant-update");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification to inform the requester about the update on the variant has been accepted and send for the approval");
        register.setUserTypes(List.of(UserType.SELLER));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/variant/variant-update");
        emailRegister.setSubject("Your Variant Update got accepted and In Approval - ${variantId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);
    }
}
