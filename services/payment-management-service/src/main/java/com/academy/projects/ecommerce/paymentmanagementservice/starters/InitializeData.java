package com.academy.projects.ecommerce.paymentmanagementservice.starters;

import com.academy.projects.ecommerce.paymentmanagementservice.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final PaymentRepository paymentRepository;

    @Autowired
    public InitializeData(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    @SuppressWarnings("NullableProblems")
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;
        paymentRepository.deleteAll();
        alreadySetup = true;
    }
}
