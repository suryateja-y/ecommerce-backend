package com.academy.projects.ecommerce.productsearchservice.kafka.consumer.services;

import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.SellerDto;
import com.academy.projects.ecommerce.productsearchservice.services.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SellerConsumer {

    private final ISellerService sellerService;

    @Autowired
    public SellerConsumer(ISellerService sellerService) {
        this.sellerService = sellerService;
    }

    @KafkaListener(topics = "${application.kafka.topics.seller-update-topic}", groupId = "${application.kafka.consumer.seller-update-group}", containerFactory = "kafkaListenerContainerFactoryForSeller")
    public void consumer(SellerDto sellerDto) {
        try {
            sellerService.update(sellerDto);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
