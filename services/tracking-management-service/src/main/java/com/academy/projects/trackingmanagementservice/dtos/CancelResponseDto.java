package com.academy.projects.trackingmanagementservice.dtos;

import com.academy.projects.trackingmanagementservice.models.OrderPackage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class CancelResponseDto implements Serializable {
    private OrderPackage orderPackage;
    private ActionStatus responseStatus;
    private String responseMessage;
}
