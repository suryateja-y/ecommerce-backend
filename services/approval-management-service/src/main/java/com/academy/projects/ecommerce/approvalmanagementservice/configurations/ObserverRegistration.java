package com.academy.projects.ecommerce.approvalmanagementservice.configurations;

import com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.observers.NotificationObserver;
import com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.observers.RequestServiceObserver;
import com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.publishers.IApprovalUpdatePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverRegistration {

    private final IApprovalUpdatePublisher approvalUpdatePublisher;
    private final RequestServiceObserver requestServiceObserver;
    private final NotificationObserver notificationObserver;

    private final Logger logger = LoggerFactory.getLogger(ObserverRegistration.class);

    @Autowired
    public ObserverRegistration(IApprovalUpdatePublisher approvalUpdatePublisher, RequestServiceObserver requestServiceObserver, NotificationObserver notificationObserver) {
        this.approvalUpdatePublisher = approvalUpdatePublisher;
        this.requestServiceObserver = requestServiceObserver;
        this.notificationObserver = notificationObserver;
    }

    @Bean
    public boolean registerUpdateObservers() {
        approvalUpdatePublisher.addObserver(requestServiceObserver);
        approvalUpdatePublisher.addObserver(notificationObserver);
        logger.info("Approval Update Observers Registered Successfully!!!");
        return true;
    }
}
