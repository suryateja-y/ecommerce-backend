package com.academy.projects.trackingmanagementservice.repositories;

import com.academy.projects.trackingmanagementservice.models.OrderPackage;
import com.academy.projects.trackingmanagementservice.models.TrackingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PackageRepository extends MongoRepository<OrderPackage, String> {
    Optional<OrderPackage> findByTrackingNumberOrId(String trackingNumber, String trackingNumber1);
    Page<OrderPackage> findAllByOrderIdAndTrackingStatusAndEtaBetween(String orderId, TrackingStatus trackingStatus, Date from, Date To, Pageable pageable);
    Page<OrderPackage> findAllByOrderIdAndTrackingStatus(String orderId, TrackingStatus trackingStatus, Pageable pageable);
    Page<OrderPackage> findAllByOrderIdAndEtaBetween(String orderId, Date from, Date to, Pageable pageable);
    Page<OrderPackage> findAllByOrderId(String orderId, Pageable pageable);
    Page<OrderPackage> findAllByTrackingStatusAndEtaBetween(TrackingStatus trackingStatus, Date from, Date to, Pageable pageable);
    Page<OrderPackage> findAllByTrackingStatus(TrackingStatus trackingStatus, Pageable pageable);
    Page<OrderPackage> findAllByEtaBetween(Date from, Date to, Pageable pageable);
}
