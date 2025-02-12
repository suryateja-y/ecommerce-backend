package com.academy.projects.ecommerce.notificationservice.kafka.consumers.services;

import com.academy.projects.ecommerce.notificationservice.exceptions.InvalidActionException;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.ApprovalStatus;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.Product;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.ProductDto;
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
public class ProductManagementService {

    private final IUserService userService;
    private final INotificationManagementService notificationManagementService;

    private final Logger logger = LoggerFactory.getLogger(ProductManagementService.class);

    @Autowired
    public ProductManagementService(IUserService userService, INotificationManagementService notificationManagementService) {
        this.userService = userService;
        this.notificationManagementService = notificationManagementService;
    }

    @SuppressWarnings("DuplicatedCode")
    @KafkaListener(topics = "${application.kafka.topics.product-topic}", groupId = "${application.kafka.consumer.product-group}", containerFactory = "kafkaListenerContainerFactoryForProduct")
    public void consumeProduct(ProductDto productDto) {
        try {
            User user = userService.getByUserid(productDto.getSellerId());
            switch (productDto.getAction()) {
                case CREATE, UPDATE:
                    sendCreateUpdate(productDto, user);
                    break;
                case DELETE:
                    sendDelete(productDto);
                    break;
                case STATUS_UPDATE:
                    sendStatus(productDto, user);
                    break;
                default:
                    throw new InvalidActionException(productDto.getAction());
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void sendCreateUpdate(ProductDto productDto, User user) {
        String registryKey = getRegistryKey(productDto.getAction());
        RecipientCommunicationDetails communicationDetails = getCommunicationDetails(user.getEmail());
        notificationManagementService.send(registryKey, prepareData(productDto, user), communicationDetails);
        logger.info("Sent product notification to '{}'", user.getEmail());
    }

    private void sendDelete(ProductDto productDto) {
        List<User> sellers = userService.get(UserType.SELLER, UserState.APPROVED);
        String registryKey = getRegistryKey(Action.DELETE);
        for(User seller : sellers) {
            RecipientCommunicationDetails communicationDetails = getCommunicationDetails(seller.getEmail());
            notificationManagementService.send(registryKey, prepareData(productDto, seller), communicationDetails);
            logger.info("Sent product delete notification to '{}'", seller.getEmail());
        }
    }

    private void sendStatus(ProductDto productDto, User user) {
        Product product = productDto.getProduct();
        if(product.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
            sendApproved(productDto, user);
        } else {
            sendCreateUpdate(productDto, user);
        }
    }

    public void sendApproved(ProductDto productDto, User user) {
        Map<String, Object> data = prepareData(productDto, user);
        RecipientCommunicationDetails communicationDetails = getCommunicationDetails(user.getEmail());
        sendApproved("product-approved", data, communicationDetails);
        logger.info("Sent product approved notification to '{}'", user.getEmail());

        List<User> sellers = userService.get(UserType.SELLER, UserState.APPROVED);
        String registryKey = "product-approved-seller";
        for(User seller : sellers) {
            data.put("userName", seller.getFullName());
            communicationDetails = getCommunicationDetails(seller.getEmail());
            notificationManagementService.send(registryKey, data, communicationDetails);
            logger.info("Sent product added notification to '{}'", seller.getEmail());
        }
    }

    public void sendApproved(String registryKey, Map<String, Object> data, RecipientCommunicationDetails communicationDetails) {
        notificationManagementService.send(registryKey, data, communicationDetails);
    }

    private String getRegistryKey(Action action) {
        return switch (action) {
            case CREATE -> "product-create";
            case UPDATE -> "product-update";
            case DELETE -> "product-delete";
            case STATUS_UPDATE -> "product-status";
            default -> throw new InvalidActionException(action);
        };
    }
    private RecipientCommunicationDetails getCommunicationDetails(String email) {
        return RecipientCommunicationDetails.builder()
                .email(email)
                .build();
    }
    private Map<String, Object> prepareData(ProductDto productDto, User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("userName", user.getFullName());
        data.put("productId", productDto.getProduct().getId());
        data.put("productName", productDto.getProduct().getName());
        data.put("productDesc", productDto.getProduct().getDescription());
        data.put("category", productDto.getProduct().getCategory());
        data.put("approvalId", productDto.getProduct().getApprovalId());
        return data;
    }
}
