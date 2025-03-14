package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.models.PaymentDetails;

public interface IPaymentDetailsService {
    PaymentDetails save(PaymentDetails paymentDetails);
}
