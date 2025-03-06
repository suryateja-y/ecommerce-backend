package com.academy.projects.trackingmanagementservice.kafka.consumers.services;

import com.academy.projects.trackingmanagementservice.dtos.CancelResponseDto;
import com.academy.projects.trackingmanagementservice.kafka.dtos.OrderDto;
import com.academy.projects.trackingmanagementservice.kafka.dtos.OrderItem;
import com.academy.projects.trackingmanagementservice.kafka.producers.services.ITrackingUpdateManager;
import com.academy.projects.trackingmanagementservice.models.PackageRequest;
import com.academy.projects.trackingmanagementservice.services.IPackageRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderManagementService {
    private final IPackageRequestService packageRequestService;
    private final ITrackingUpdateManager trackingUpdateManager;

    private final Logger logger = LoggerFactory.getLogger(OrderManagementService.class);

    @Autowired
    public OrderManagementService(IPackageRequestService packageRequestService, ITrackingUpdateManager trackingUpdateManager) {
        this.packageRequestService = packageRequestService;
        this.trackingUpdateManager = trackingUpdateManager;
    }

    @KafkaListener(topics = "${application.kafka.topics.order-topic}", groupId = "${application.kafka.consumer.order-tracking-group}", containerFactory = "kafkaListenerContainerFactoryForOrder")
    public void consumer(OrderDto orderDto) {
        try {
            switch (orderDto.getAction()) {
                case CREATE: createPackage(orderDto); break;
                case CANCELLED: cancelPackage(orderDto); break;
                default: logger.error("Invalid Order Action: '{}'", orderDto.getAction());
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createPackage(OrderDto orderDto) {
        packageRequestService.create(from(orderDto));
        logger.info("Created Package Request for the Order '{}'", orderDto.getOrderId());
    }

    private void cancelPackage(OrderDto orderDto) {
        CancelResponseDto cancelResponseDto = packageRequestService.cancel(orderDto.getOrderId());
        // Send confirmation to the Order Management Service
        trackingUpdateManager.sendCancelUpdate(cancelResponseDto, orderDto.getAction());
        logger.info("Canceled Package Request for the Order '{}'", orderDto.getOrderId());
    }

    private PackageRequest from(OrderDto orderDto) {
        PackageRequest packageRequest = new PackageRequest();
        packageRequest.setOrderId(orderDto.getOrderId());
        packageRequest.setDeliveryAddressId(orderDto.getShippingAddressId());
        packageRequest.setSellerId(getSellerId(orderDto));
        packageRequest.setEta(getETA(orderDto));
        packageRequest.setCustomerId(orderDto.getCustomerId());
        return packageRequest;
    }

    private String getSellerId(OrderDto orderDto) {
        return orderDto.getOrderItems().stream().findFirst().map(OrderItem::getSellerId).orElse("");
    }

    private Date getETA(OrderDto orderDto) {
        return orderDto.getOrderItems().stream().findFirst().map(OrderItem::getEta).orElse(null);
    }
}
