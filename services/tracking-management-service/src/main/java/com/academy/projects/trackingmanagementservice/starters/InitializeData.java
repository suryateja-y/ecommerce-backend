package com.academy.projects.trackingmanagementservice.starters;

import com.academy.projects.trackingmanagementservice.models.*;
import com.academy.projects.trackingmanagementservice.repositories.PackageRepository;
import com.academy.projects.trackingmanagementservice.repositories.PackageRequestRepository;
import com.academy.projects.trackingmanagementservice.repositories.TrackingNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final PackageRequestRepository packageRequestRepository;
    private final PackageRepository packageRepository;
    private final TrackingNumberRepository trackingNumberRepository;

    @Autowired
    public InitializeData(PackageRequestRepository packageRequestRepository, PackageRepository packageRepository, TrackingNumberRepository trackingNumberRepository) {
        this.packageRequestRepository = packageRequestRepository;
        this.packageRepository = packageRepository;
        this.trackingNumberRepository = trackingNumberRepository;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        packageRequestRepository.deleteAll();
        packageRepository.deleteAll();
        trackingNumberRepository.deleteAll();

        alreadySetup = true;
    }
}
