package com.academy.projects.ecommerce.notificationservice.kafka.consumers.services;

import com.academy.projects.ecommerce.notificationservice.exceptions.InvalidActionException;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.ApprovalStatus;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.Variant;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.VariantDto;
import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.User;
import com.academy.projects.ecommerce.notificationservice.models.UserState;
import com.academy.projects.ecommerce.notificationservice.models.UserType;
import com.academy.projects.ecommerce.notificationservice.services.notifications.INotificationManagementService;
import com.academy.projects.ecommerce.notificationservice.services.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VariantManagementService {
    private final IUserService userService;
    private final INotificationManagementService notificationManagementService;

    private final Logger logger = LoggerFactory.getLogger(VariantManagementService.class);

    @Autowired
    public VariantManagementService(IUserService userService, INotificationManagementService notificationManagementService) {
        this.userService = userService;
        this.notificationManagementService = notificationManagementService;
    }

    @SuppressWarnings("DuplicatedCode")
    @KafkaListener(topics = "${application.kafka.topics.variant-topic}", groupId = "${application.kafka.consumer.variant-group}", containerFactory = "kafkaListenerContainerFactoryForVariant")
    public void consumeVariant(VariantDto variantDto) {
        try {
            User user = userService.getByUserid(variantDto.getSellerId());
            switch (variantDto.getAction()) {
                case CREATE, UPDATE:
                    sendCreateUpdate(variantDto, user);
                    break;
                case DELETE:
                    sendDelete(variantDto);
                    break;
                case STATUS_UPDATE:
                    sendStatus(variantDto, user);
                    break;
                default:
                    throw new InvalidActionException(variantDto.getAction());
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void sendCreateUpdate(VariantDto variantDto, User user) {
        String registryKey = getRegistryKey(variantDto.getAction());
        RecipientCommunicationDetails communicationDetails = getCommunicationDetails(user.getEmail());
        notificationManagementService.send(registryKey, prepareData(variantDto, user), communicationDetails);
        logger.info("Sent Variant notification to '{}'", user.getEmail());
    }

    private void sendDelete(VariantDto variantDto) {
        List<User> sellers = userService.get(UserType.SELLER, UserState.APPROVED);
        String registryKey = getRegistryKey(Action.DELETE);
        for(User seller : sellers) {
            RecipientCommunicationDetails communicationDetails = getCommunicationDetails(seller.getEmail());
            notificationManagementService.send(registryKey, prepareData(variantDto, seller), communicationDetails);
            logger.info("Sent Variant delete notification to '{}'", seller.getEmail());
        }
    }

    private void sendStatus(VariantDto variantDto, User user) {
        Variant variant = variantDto.getVariant();
        if(variant.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
            sendApproved(variantDto, user);
        } else {
            sendCreateUpdate(variantDto, user);
        }
    }

    public void sendApproved(VariantDto variantDto, User user) {
        Map<String, Object> data = prepareData(variantDto, user);
        RecipientCommunicationDetails communicationDetails = getCommunicationDetails(user.getEmail());
        sendApproved("variant-approved", data, communicationDetails);
        logger.info("Sent Variant approved notification to '{}'", user.getEmail());

        List<User> sellers = userService.get(UserType.SELLER, UserState.APPROVED);
        String registryKey = "variant-approved-seller";
        for(User seller : sellers) {
            data.put("userName", seller.getFullName());
            communicationDetails = getCommunicationDetails(seller.getEmail());
            notificationManagementService.send(registryKey, data, communicationDetails);
            logger.info("Sent Variant added notification to '{}'", seller.getEmail());
        }
    }

    public void sendApproved(String registryKey, Map<String, Object> data, RecipientCommunicationDetails communicationDetails) {
        notificationManagementService.send(registryKey, data, communicationDetails);
    }

    private String getRegistryKey(Action action) {
        return switch (action) {
            case CREATE -> "variant-create";
            case UPDATE -> "variant-update";
            case DELETE -> "variant-delete";
            case STATUS_UPDATE -> "variant-status";
            default -> throw new InvalidActionException(action);
        };
    }
    private RecipientCommunicationDetails getCommunicationDetails(String email) {
        return RecipientCommunicationDetails.builder()
                .email(email)
                .build();
    }
    private Map<String, Object> prepareData(VariantDto variantDto, User user) {
        Map<String, Object> data = new HashMap<>();
        Variant variant = variantDto.getVariant();
        data.put("userName", user.getFullName());
        data.put("variantId", variant.getId());
        data.put("productId", variant.getProduct().getId());
        data.put("productName", variant.getProduct().getName());
        data.put("productDesc", variant.getProduct().getDescription());
        data.put("category", variant.getProduct().getCategory());
        data.put("approvalId", variant.getApprovalId());
        return data;
    }
}
