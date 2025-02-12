package com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user;

import com.academy.projects.ecommerce.usermanagementservice.models.Customer;

public interface ICustomerUpdateManager {
    void sendRegistration(Customer customer);
    void sendUpdate(Customer customer);
}
