package com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user;

import com.academy.projects.ecommerce.usermanagementservice.models.Seller;

public interface ISellerUpdateManager {
    void sendRegistration(Seller seller);
    void sendUpdate(Seller seller);
    void sendAddressUpdate(Seller seller);
    void sendStatusUpdate(Seller seller);
}
