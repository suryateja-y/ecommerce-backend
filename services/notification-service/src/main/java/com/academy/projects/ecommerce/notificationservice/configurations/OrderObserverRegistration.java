package com.academy.projects.ecommerce.notificationservice.configurations;

import com.academy.projects.ecommerce.notificationservice.publishers.OrderPublisher;
import com.academy.projects.ecommerce.notificationservice.services.services.IOrderNotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderObserverRegistration {
    private final OrderPublisher orderPublisher;
    private final IOrderNotificationService orderNotificationService;

    public OrderObserverRegistration(OrderPublisher orderPublisher, IOrderNotificationService orderNotificationService) {
        this.orderPublisher = orderPublisher;
        this.orderNotificationService = orderNotificationService;
    }
    @Bean
    public boolean registerOrderObserver() {
        orderPublisher.addObserver(orderNotificationService);
        return true;
    }
}
