package com.academy.projects.ecommerce.notificationservice.starters;

import com.academy.projects.ecommerce.notificationservice.repositories.NotificationRepository;
import com.academy.projects.ecommerce.notificationservice.repositories.SettingRepository;
import com.academy.projects.ecommerce.notificationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@SuppressWarnings("NullableProblems")
@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup;
    private final InitiateUsers initiateUsers;
    private final InitializeApprovalNotifications initializeApprovalNotifications;
    private final InitializeCategoryNotifications initializeCategoryNotifications;
    private final InitializeInventoryNotifications initializeInventoryNotifications;
    private final InitializeOrderNotifications initializeOrderNotifications;
    private final InitializePaymentNotifications initializePaymentNotifications;
    private final InitializeProductNotifications initializeProductNotifications;
    private final InitializeUserNotifications initializeUserNotifications;
    private final InitializeVariantNotifications initializeVariantNotifications;

    private final NotificationRepository notificationRepository;

    @Autowired
    public InitializeData(SettingRepository registerRepository, UserRepository userRepository, NotificationRepository notificationRepository, InitiateUsers initiateUsers, InitializeApprovalNotifications initializeApprovalNotifications, InitializeCategoryNotifications initializeCategoryNotifications, InitializeInventoryNotifications initializeInventoryNotifications, InitializeOrderNotifications initializeOrderNotifications, InitializePaymentNotifications initializePaymentNotifications, InitializeProductNotifications initializeProductNotifications, InitializeUserNotifications initializeUserNotifications, InitializeVariantNotifications initializeVariantNotifications) {
        registerRepository.deleteAll();
        userRepository.deleteAll();
        notificationRepository.deleteAll();
        this.initiateUsers = initiateUsers;
        this.initializeApprovalNotifications = initializeApprovalNotifications;
        this.initializeCategoryNotifications = initializeCategoryNotifications;
        this.initializeInventoryNotifications = initializeInventoryNotifications;
        this.initializeOrderNotifications = initializeOrderNotifications;
        this.initializePaymentNotifications = initializePaymentNotifications;
        this.initializeProductNotifications = initializeProductNotifications;
        this.initializeUserNotifications = initializeUserNotifications;
        this.initializeVariantNotifications = initializeVariantNotifications;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;
        this.notificationRepository.deleteAll();

        initiateUsers.initialize();
        initializeApprovalNotifications.initialize();
        initializeCategoryNotifications.initialize();
        initializeInventoryNotifications.initialize();
        initializeOrderNotifications.initialize();
        initializePaymentNotifications.initialize();
        initializeProductNotifications.initialize();
        initializeUserNotifications.initialize();
        initializeVariantNotifications.initialize();

        alreadySetup = true;
    }
}
