package com.academy.projects.ecommerce.notificationservice.kafka.consumers.services;

import com.academy.projects.ecommerce.notificationservice.exceptions.InvalidActionException;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.Category;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.CategoryDto;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryManagementService {

    private final IUserService userService;
    private final INotificationManagementService notificationManagementService;
    private final Logger logger = LoggerFactory.getLogger(CategoryManagementService.class);

    @Autowired
    public CategoryManagementService(IUserService userService, INotificationManagementService notificationManagementService) {
        this.userService = userService;
        this.notificationManagementService = notificationManagementService;
    }

    @KafkaListener(topics = "${application.kafka.topics.category-topic}", groupId = "${application.kafka.consumer.category-group}", containerFactory = "kafkaListenerContainerFactoryForCategory")
    public void consumeCategory(CategoryDto categoryDto) {
        try {
            String registryKey = getRegistryKey(categoryDto.getAction());
            List<RecipientCommunicationDetails> communicationDetails = getCommunicationDetails();
            Map<String, Object> data = prepareData(categoryDto.getCategory());
            for (RecipientCommunicationDetails communicationDetail : communicationDetails) {
                notificationManagementService.send(registryKey, data, communicationDetail);
                logger.info("Sent Category Update to the Seller: '{}'", communicationDetail.getEmail());
            }
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getRegistryKey(Action action) {
        return switch (action) {
            case CREATE -> "category-create";
            case UPDATE -> "category-update";
            case DELETE -> "category-delete";
            default -> throw new InvalidActionException(action);
        };
    }

    private List<RecipientCommunicationDetails> getCommunicationDetails() {
        List<User> activeSellers = userService.get(UserType.SELLER, UserState.APPROVED);
        return activeSellers.stream().map(activeSeller -> RecipientCommunicationDetails.builder().email(activeSeller.getEmail()).build()).toList();
    }

    private Map<String, Object> prepareData(Category category) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("categoryId", category.getId());
        data.put("categoryName", category.getCategoryName());
        data.put("categoryDescription", category.getDescription());
        data.put("hlCategory", category.getHighLevelCategory());
        return data;
    }
}
