package com.academy.projects.ecommerce.approvalmanagementservice.kafka.consumer;

import com.academy.projects.ecommerce.approvalmanagementservice.kafka.dtos.UpdateStatusRequestDto;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;
import com.academy.projects.ecommerce.approvalmanagementservice.services.IApprovalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UpdateConsumer {
    private final IApprovalService approvalService;
    private final Logger logger = LoggerFactory.getLogger(UpdateConsumer.class);

    @Autowired
    public UpdateConsumer(IApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @KafkaListener(topics = "${application.kafka.topics.user-update-topic}", groupId = "${application.kafka.consumer.user-update-group}", containerFactory = "kafkaListenerContainerFactory")
    public void consumer(UpdateStatusRequestDto requestDto) {
        try {
            ApprovalRequest approvalRequest = approvalService.updateStatus(requestDto.getApprovalId(), requestDto.getApprovalStatus(), requestDto.getRequesterId());
            logger.info("Approval Request: {} has been updated!!!", approvalRequest.getId());
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }
}
