package com.academy.projects.ecommerce.notificationservice.kafka.consumers.services;

import com.academy.projects.ecommerce.notificationservice.exceptions.InvalidActionException;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.*;
import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.User;
import com.academy.projects.ecommerce.notificationservice.services.notifications.INotificationManagementService;
import com.academy.projects.ecommerce.notificationservice.services.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrackingManagementService {
    private final IUserService userService;
    private final INotificationManagementService notificationManagementService;

    private final Logger logger = LoggerFactory.getLogger(TrackingManagementService.class);

    @Autowired
    public TrackingManagementService(IUserService userService, INotificationManagementService notificationManagementService) {
        this.userService = userService;
        this.notificationManagementService = notificationManagementService;
    }
    @KafkaListener(topics = "${application.kafka.topics.tracking-topic}", groupId = "${application.kafka.consumer.tracking-group}", containerFactory = "kafkaListenerContainerFactoryForTracking")
    public void consumer(TrackingDto trackingDto) {
        try {
            User customer = userService.getByUserid(trackingDto.getCustomerId());
            String registryKey = getRegistryKey(trackingDto.getTrackingStatus());
            RecipientCommunicationDetails communicationDetails = getCommunicationDetails(customer);
            notificationManagementService.send(registryKey, prepareData(trackingDto, customer), communicationDetails);
            logger.info("Notification has been sent related to Tracking: '{}'", trackingDto.getTrackingId());
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getRegistryKey(TrackingStatus trackingStatus) {
        return switch (trackingStatus) {
            case CREATED -> "tracking-shipped";
            case OUT_FOR_DELIVERY -> "tracking-out-for-delivery";
            case DELIVERED -> "tracking-delivered";
            default -> throw new InvalidActionException(trackingStatus);
        };
    }

    private RecipientCommunicationDetails getCommunicationDetails(User customer) {
        return RecipientCommunicationDetails.builder()
                .email(customer.getEmail())
                .build();
    }

    private Map<String, Object> prepareData(TrackingDto trackingDto, User customer) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userName", customer.getFullName());
        data.put("orderId", trackingDto.getOrderId());
        data.put("trackingId", trackingDto.getTrackingNumber());
        return data;
    }
}
