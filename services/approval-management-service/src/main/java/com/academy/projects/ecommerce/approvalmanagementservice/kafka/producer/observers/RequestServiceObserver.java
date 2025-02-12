package com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.observers;

import com.academy.projects.ecommerce.approvalmanagementservice.kafka.dtos.UpdateType;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceObserver implements ApprovalUpdateObserver {

    private final KafkaTemplate<String, ApprovalRequest> approvalKafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(RequestServiceObserver.class);

    @Autowired
    public RequestServiceObserver(final KafkaTemplate<String, ApprovalRequest> approvalKafkaTemplate) {
        this.approvalKafkaTemplate = approvalKafkaTemplate;
    }

    @Override
    public void sendUpdate(ApprovalRequest request, UpdateType updateType) {
        if(request.getStatus().equals(ApprovalStatus.APPROVED)) {
            approvalKafkaTemplate.send(request.getTopic(), request);
            logger.info("Update has been sent to the topic: {}", request.getTopic());
        }
    }
}
