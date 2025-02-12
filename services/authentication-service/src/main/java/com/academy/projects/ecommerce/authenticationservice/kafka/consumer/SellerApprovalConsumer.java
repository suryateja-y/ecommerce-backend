package com.academy.projects.ecommerce.authenticationservice.kafka.consumer;

import com.academy.projects.ecommerce.authenticationservice.kafka.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.authenticationservice.services.authentication.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SellerApprovalConsumer {

    public final IUserService userService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SellerApprovalConsumer(final IUserService userService) {
        this.userService = userService;
    }

    @KafkaListener(topics = "${application.kafka.topics.user-approval-topic}", groupId = "${application.kafka.user.user-approval-group}", containerFactory = "kafkaListenerContainerFactory")
    public void consumer(ApprovalRequest approvalRequest) {
        try {
            this.userService.changeState(approvalRequest);
        } catch(Exception e) {
            // Revert back to approval service
            logger.error(e.getMessage());
        }
    }
}
