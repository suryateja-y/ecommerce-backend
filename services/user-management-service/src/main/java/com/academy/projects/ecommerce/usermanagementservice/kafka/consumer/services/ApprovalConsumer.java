package com.academy.projects.ecommerce.usermanagementservice.kafka.consumer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.academy.projects.ecommerce.usermanagementservice.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.usermanagementservice.kafka.dtos.UserWrapperDto;
import com.academy.projects.ecommerce.usermanagementservice.models.User;
import com.academy.projects.ecommerce.usermanagementservice.models.UserType;
import com.academy.projects.ecommerce.usermanagementservice.services.approvalmanagement.UserApprovalManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ApprovalConsumer {
    private final UserApprovalManagerFactory userApprovalManagerFactory;
    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(ApprovalConsumer.class);

    @Autowired
    public ApprovalConsumer(UserApprovalManagerFactory userApprovalManagerFactory, ObjectMapper objectMapper) {
        this.userApprovalManagerFactory = userApprovalManagerFactory;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${application.kafka.topics.user-approval-topic}", groupId = "${application.kafka.consumer.user.user-approval-group}", containerFactory = "kafkaListenerContainerFactory")
    public void consumer(ApprovalRequest approvalRequest) {
        try {
            userApprovalManagerFactory.getApprovalManager(getUserType(approvalRequest)).updateStatus(approvalRequest);
        } catch(Exception e) {
            logger.error(e.getMessage());
            // Revert back to the approval management service
        }
    }

    private UserType getUserType(ApprovalRequest approvalRequest) {
        UserWrapperDto userWrapperDto = objectMapper.convertValue(approvalRequest.getData(), UserWrapperDto.class);
        User user = userWrapperDto.getUser();
        return user.getUserType();
    }
}
