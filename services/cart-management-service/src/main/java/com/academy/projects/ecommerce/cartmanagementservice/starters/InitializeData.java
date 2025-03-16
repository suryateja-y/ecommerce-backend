package com.academy.projects.ecommerce.cartmanagementservice.starters;

import com.academy.projects.ecommerce.cartmanagementservice.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    private final CartRepository cartRepository;

    @Autowired
    public InitializeData(final CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        cartRepository.deleteAll();
        alreadySetup = true;
    }
}
