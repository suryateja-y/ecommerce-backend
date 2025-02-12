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
public class InitializeApprovalNotifications {
    private final SettingRepository settingRepository;

    @Autowired
    public InitializeApprovalNotifications(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public void initialize() {

        // approval-registration
        Setting register = new Setting();
        register.setRegistryKey("approval-registration");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the requester when the approval request got registered in the Approval Management Service");
        register.setUserTypes(List.of(UserType.SELLER, UserType.EMPLOYEE));
        EmailRegister emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/approvals/approval-registration");
        emailRegister.setSubject("Approval Registration - ${approvalId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // approval-update
        register = new Setting();
        register.setRegistryKey("approval-update");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the requester confirming the update made to the Approval Request in the Approval Management Service");
        register.setUserTypes(List.of(UserType.SELLER, UserType.EMPLOYEE));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/approvals/approval-update");
        emailRegister.setSubject("Approval Update - ${approvalId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // approved
        register = new Setting();
        register.setRegistryKey("approved");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the requester informing the Approval Request has been approved and completed in the Approval Management Service");
        register.setUserTypes(List.of(UserType.SELLER, UserType.EMPLOYEE));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/approvals/approved");
        emailRegister.setSubject("APPROVED - ${approvalId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // approver-update
        register = new Setting();
        register.setRegistryKey("approver-update");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the approver got assigned to review and approve the Approval Request in the Approval Management Service");
        register.setUserTypes(List.of(UserType.EMPLOYEE));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/approvals/approver-update");
        emailRegister.setSubject("Approval Request Assigned - ${approvalId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

        // status-update
        register = new Setting();
        register.setRegistryKey("status-update");
        register.setNotificationTypes(List.of(NotificationType.EMAIL));
        register.setDescription("Notification send to the requester if any update to the Approval Request in the Approval Management Service");
        register.setUserTypes(List.of(UserType.SELLER, UserType.EMPLOYEE));
        emailRegister = new EmailRegister();
        emailRegister.setTemplateName("/email/approvals/status-update");
        emailRegister.setSubject("Approval Request - Status Update - ${approvalId}");
        register.setEmailRegister(emailRegister);
        settingRepository.save(register);

    }
}
