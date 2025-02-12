package com.academy.projects.ecommerce.paymentmanagementservice.services;

public interface IWebHookService {
    void handleEvent(String payload, String signature);
}