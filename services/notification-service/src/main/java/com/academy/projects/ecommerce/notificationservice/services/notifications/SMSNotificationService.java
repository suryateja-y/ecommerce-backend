package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SMSNotificationService implements ISMSNotificationService {
    private final Logger logger = LoggerFactory.getLogger(SMSNotificationService.class);

    @Override
    public void send(Setting register, Map<String, Object> data, RecipientCommunicationDetails recipientCommunicationDetails) {
        logger.info("SMS Notification Service is yet to implement!!!");
    }
}
