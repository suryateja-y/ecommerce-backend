package com.academy.projects.ecommerce.approvalmanagementservice.kafka.producer.observers;

import com.academy.projects.ecommerce.approvalmanagementservice.kafka.dtos.NotificationDto;
import com.academy.projects.ecommerce.approvalmanagementservice.kafka.dtos.UpdateType;
import com.academy.projects.ecommerce.approvalmanagementservice.models.ApprovalRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationObserver implements ApprovalUpdateObserver {
    @Value("${application.kafka.topics.approval-notification-topic}")
    private String notificationTopic;

    private final KafkaTemplate<String, NotificationDto> notificationKafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(NotificationObserver.class);

    @Autowired
    public NotificationObserver(KafkaTemplate<String, NotificationDto> notificationKafkaTemplate) {
        this.notificationKafkaTemplate = notificationKafkaTemplate;
    }

    @Override
    public void sendUpdate(ApprovalRequest request, UpdateType updateType) {
        NotificationDto notificationDto = NotificationDto.builder()
                .requestId(request.getId())
                .title(request.getTitle())
                .previousAssignee(request.getPreviousAssignedTo())
                .updateType(updateType)
                .approvalStatus(request.getStatus())
                .currentAssignee(request.getAssignedTo())
                .requester(request.getRequester())
                .build();
        notificationKafkaTemplate.send(notificationTopic, notificationDto);
        logger.info("Approval update has been sent to Notification Service: {}", request.getId());
    }
}
