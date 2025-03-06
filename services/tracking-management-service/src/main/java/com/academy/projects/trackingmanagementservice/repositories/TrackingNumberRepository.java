package com.academy.projects.trackingmanagementservice.repositories;

import com.academy.projects.trackingmanagementservice.models.TrackingNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackingNumberRepository extends MongoRepository<TrackingNumber, String> {
    Optional<Long> findTrackingNumberById(String trackingNumber);
}
