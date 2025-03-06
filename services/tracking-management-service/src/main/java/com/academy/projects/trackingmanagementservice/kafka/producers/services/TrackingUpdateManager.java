package com.academy.projects.trackingmanagementservice.kafka.producers.services;

import com.academy.projects.trackingmanagementservice.dtos.ActionStatus;
import com.academy.projects.trackingmanagementservice.dtos.CancelResponseDto;
import com.academy.projects.trackingmanagementservice.kafka.dtos.Action;
import com.academy.projects.trackingmanagementservice.kafka.dtos.TrackingDto;
import com.academy.projects.trackingmanagementservice.models.OrderPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrackingUpdateManager implements ITrackingUpdateManager {
    @Value("${application.kafka.topics.tracking-topic}")
    private String trackingTopic;

    private final KafkaTemplate<String, TrackingDto> trackingKafkaTemplate;

    @Autowired
    public TrackingUpdateManager(KafkaTemplate<String, TrackingDto> trackingKafkaTemplate) {
        this.trackingKafkaTemplate = trackingKafkaTemplate;
    }

    @Override
    public void sendCancelUpdate(CancelResponseDto cancelResponseDto, Action action) {
        if(!cancelResponseDto.getResponseStatus().equals(ActionStatus.NEUTRAL)) {
            OrderPackage orderPackage = cancelResponseDto.getOrderPackage();
            TrackingDto trackingDto = TrackingDto.builder()
                    .orderId(orderPackage.getOrderId())
                    .action(action)
                    .actionStatus(cancelResponseDto.getResponseStatus())
                    .trackingStatus(cancelResponseDto.getOrderPackage().getTrackingStatus())
                    .message(cancelResponseDto.getResponseMessage())
                    .trackingId(orderPackage.getId())
                    .trackingNumber(orderPackage.getTrackingNumber())
                    .build();
            trackingKafkaTemplate.send(trackingTopic, trackingDto);
        }
    }

    @Override
    public void sendUpdate(OrderPackage orderPackage, Action action) {
        TrackingDto trackingDto = from(orderPackage, action);
        trackingKafkaTemplate.send(trackingTopic, trackingDto);
    }

    private TrackingDto from(OrderPackage orderPackage, Action action) {
        return TrackingDto.builder()
                .orderId(orderPackage.getOrderId())
                .action(action)
                .actionStatus(ActionStatus.SUCCEEDED)
                .trackingStatus(orderPackage.getTrackingStatus())
                .message("Status Update")
                .trackingId(orderPackage.getId())
                .trackingNumber(orderPackage.getTrackingNumber())
                .build();
    }
}
