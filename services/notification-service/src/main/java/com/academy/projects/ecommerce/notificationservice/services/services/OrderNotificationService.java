package com.academy.projects.ecommerce.notificationservice.services.services;

import com.academy.projects.ecommerce.notificationservice.kafka.dtos.OrderDto;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationService implements IOrderNotificationService {
    @Override
    public void orderUpdate(OrderDto order) {
        
    }
}
