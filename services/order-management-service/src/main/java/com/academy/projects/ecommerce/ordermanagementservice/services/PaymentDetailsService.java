package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.models.PaymentDetails;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.PaymentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsService implements IPaymentDetailsService {
    private final PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    public PaymentDetailsService(PaymentDetailsRepository paymentDetailsRepository) {
        this.paymentDetailsRepository = paymentDetailsRepository;
    }
    @Override
    public PaymentDetails save(PaymentDetails paymentDetails) {
        return paymentDetailsRepository.save(paymentDetails);
    }
}
