package com.academy.projects.ecommerce.paymentmanagementservice.services;

import com.academy.projects.ecommerce.paymentmanagementservice.dtos.PaymentInitiationRequest;
import com.academy.projects.ecommerce.paymentmanagementservice.kafka.dtos.PaymentIntentDto;
import com.academy.projects.ecommerce.paymentmanagementservice.models.Payment;
import com.academy.projects.ecommerce.paymentmanagementservice.models.PaymentStatus;

import java.util.List;

public interface IPaymentService {
    Payment initiatePayment(PaymentInitiationRequest paymentInitiationRequest);
    List<Payment> get(String customerId, String orderId, PaymentStatus paymentStatus, int page, int pageSize);
    void update(PaymentIntentDto paymentIntentDto);
    Payment cancel(PaymentInitiationRequest from);
    Payment refund(PaymentInitiationRequest from);
}
