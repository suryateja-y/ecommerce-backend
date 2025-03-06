package com.academy.projects.trackingmanagementservice.kafka.producers.services;

import com.academy.projects.trackingmanagementservice.dtos.CancelResponseDto;
import com.academy.projects.trackingmanagementservice.kafka.dtos.Action;
import com.academy.projects.trackingmanagementservice.models.OrderPackage;

public interface ITrackingUpdateManager {
    void sendCancelUpdate(CancelResponseDto cancelResponseDto, Action action);
    void sendUpdate(OrderPackage orderPackage, Action action);
}
