package com.academy.projects.ecommerce.notificationservice.kafka.consumers.services;

import com.academy.projects.ecommerce.notificationservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.ApprovalNotificationDto;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.ApprovalStatus;
import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.User;
import com.academy.projects.ecommerce.notificationservice.services.notifications.INotificationManagementService;
import com.academy.projects.ecommerce.notificationservice.services.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ApprovalManagementService {

    private final IUserService userService;
    private final INotificationManagementService notificationManagementService;
    private final Logger logger = LoggerFactory.getLogger(ApprovalManagementService.class);

    @Autowired
    public ApprovalManagementService(IUserService userService, INotificationManagementService notificationManagementService) {
        this.userService = userService;
        this.notificationManagementService = notificationManagementService;
    }

    @KafkaListener(topics = "${application.kafka.topics.approval-notification-topic}", groupId = "${application.kafka.consumer.approval-notification-group}", containerFactory = "kafkaListenerContainerFactoryForApproval")
    public void consumeCustomer(ApprovalNotificationDto approvalNotificationDto) {
        try {
            if(approvalNotificationDto.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
                sendApprovedNotification(approvalNotificationDto);
            } else {
                // Send to the assigned person
                sendToAssignee(approvalNotificationDto);

                // Send to the requester
                sendToRequester(approvalNotificationDto);
            }

        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void sendApprovedNotification(ApprovalNotificationDto approvalNotificationDto) {
        User user = userService.getByUserid(approvalNotificationDto.getRequester());
        RecipientCommunicationDetails communicationDetails = getCommunicationDetails(user.getEmail());
        notificationManagementService.send("approved", prepareData(approvalNotificationDto, user), communicationDetails);
        logger.info("Send APPROVED notification to the requester: {}", user.getEmail());
    }

    private void sendToAssignee(ApprovalNotificationDto approvalNotificationDto) {
        String registryKey = getRegistryKey(approvalNotificationDto.getUpdateType(), UserType.APPROVER);
        User user = userService.getByUserid(approvalNotificationDto.getCurrentAssignee());
        User requester = userService.getByUserid(approvalNotificationDto.getRequester());
        RecipientCommunicationDetails communicationDetails = getCommunicationDetails(user.getEmail());
        notificationManagementService.send(registryKey, prepareData(approvalNotificationDto, user, requester), communicationDetails);
        logger.info("Send Assignment notification to the approver: {}", user.getEmail());
    }

    private void sendToRequester(ApprovalNotificationDto approvalNotificationDto) {
        String registryKey = getRegistryKey(approvalNotificationDto.getUpdateType(), UserType.REQUESTER);
        User user = userService.getByUserid(approvalNotificationDto.getRequester());
        User currentApprover = userService.getByUserid(approvalNotificationDto.getCurrentAssignee());
        User previousApprover = userService.getByUserid(approvalNotificationDto.getPreviousAssignee());
        RecipientCommunicationDetails communicationDetails = getCommunicationDetails(user.getEmail());
        notificationManagementService.send(registryKey, prepareData(approvalNotificationDto, user, previousApprover, currentApprover), communicationDetails);
        logger.info("Send Update notification to the requester: {}", user.getEmail());
    }

    private enum UserType {
        REQUESTER,
        APPROVER
    }

    private String getRegistryKey(Action action, UserType userType) {
        return switch (action) {
            case CREATE -> "approval-registration";
            case UPDATE -> "approval-update";
            case STATUS_UPDATE -> userType.equals(UserType.REQUESTER) ? "status-update" : "approver-update";
            default -> "";
        };
    }

    private RecipientCommunicationDetails getCommunicationDetails(String email) {
        return RecipientCommunicationDetails.builder()
                .email(email)
                .build();
    }

    private Map<String, Object> prepareData(ApprovalNotificationDto approvalNotificationDto, User requester, User previousApprover, User currentApprover) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userName", requester.getFullName());
        data.put("approvalId", approvalNotificationDto.getRequestId());
        data.put("title", approvalNotificationDto.getTitle());
        data.put("assignedTo", currentApprover.getFullName());
        data.put("status", approvalNotificationDto.getApprovalStatus());
        data.put("updatedBy", previousApprover.getFullName());
        return data;
    }

    private Map<String, Object> prepareData(ApprovalNotificationDto approvalNotificationDto, User currentApprover, User requester) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userName", currentApprover.getFullName());
        data.put("approvalId", approvalNotificationDto.getRequestId());
        data.put("title", approvalNotificationDto.getTitle());
        data.put("requester", requester.getFullName());
        return data;
    }

    private Map<String, Object> prepareData(ApprovalNotificationDto approvalNotificationDto, User requester) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userName", requester.getFullName());
        data.put("approvalId", approvalNotificationDto.getRequestId());
        data.put("title", approvalNotificationDto.getTitle());
        return data;
    }
}
