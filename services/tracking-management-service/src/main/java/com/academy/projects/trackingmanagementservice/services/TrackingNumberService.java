package com.academy.projects.trackingmanagementservice.services;

import com.academy.projects.trackingmanagementservice.exceptions.TrackingNumberGenerationException;
import com.academy.projects.trackingmanagementservice.models.TrackingNumber;
import com.academy.projects.trackingmanagementservice.repositories.TrackingNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrackingNumberService implements ITrackingNumberService {
    private final TrackingNumberRepository trackingNumberRepository;

    @Autowired
    public TrackingNumberService(TrackingNumberRepository trackingNumberRepository) {
        this.trackingNumberRepository = trackingNumberRepository;
    }

    @Override
    @Transactional
    public String getTrackingNumber() {
        long currentNumber = trackingNumberRepository.findTrackingNumberById("tracking_number").orElseThrow(() -> new TrackingNumberGenerationException("Failed to Generate Tracking Number!!! 'tracking_number' record not found!!!"));
        String trackingNumber = "pack-" + currentNumber;
        trackingNumberRepository.save(TrackingNumber.builder().id("tracking_number").trackingNumber(currentNumber + 1).build());
        return trackingNumber;
    }
}
