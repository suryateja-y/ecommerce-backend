package com.academy.projects.ecommerce.notificationservice.services.notifications;

import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PushNotificationService implements IPushNotificationService {
    private final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    @Override
    public void send(Setting register, Map<String, Object> data, RecipientCommunicationDetails recipientCommunicationDetails) {
        logger.info("Push Notification Service is yet to implement!!!");
    }
}
