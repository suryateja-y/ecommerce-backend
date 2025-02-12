package com.academy.projects.ecommerce.approvalmanagementservice.starters;

import com.academy.projects.ecommerce.approvalmanagementservice.repositories.ApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("NullableProblems")
@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    private final ApprovalRepository approvalRepository;

    @Autowired
    public InitializeData(ApprovalRepository approvalRepository) {
        this.approvalRepository = approvalRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;
        approvalRepository.deleteAll();
        alreadySetup = true;
    }
}
