package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.models.TrackingDetails;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.TrackingDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackingDetailsService implements ITrackingDetailsService {
    private final TrackingDetailsRepository trackingDetailsRepository;

    @Autowired
    public TrackingDetailsService(TrackingDetailsRepository trackingDetailsRepository) {
        this.trackingDetailsRepository = trackingDetailsRepository;
    }
    @Override
    public TrackingDetails save(TrackingDetails trackingDetails) {
        return trackingDetailsRepository.save(trackingDetails);
    }
}
