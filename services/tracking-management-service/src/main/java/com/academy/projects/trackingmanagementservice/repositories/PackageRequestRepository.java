package com.academy.projects.trackingmanagementservice.repositories;

import com.academy.projects.trackingmanagementservice.models.PackageRequest;
import com.academy.projects.trackingmanagementservice.models.PackageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageRequestRepository extends MongoRepository<PackageRequest, String> {
    Optional<PackageRequest> findByOrderId(String orderId);
    Page<PackageRequest> findAllByOrderIdAndCustomerIdAndSellerIdAndPackageStatus(String orderId, String customerId, String sellerId, PackageStatus packageStatus, Pageable pageable);
    Page<PackageRequest> findAllByOrderIdAndCustomerIdAndSellerId(String orderId, String customerId, String sellerId, Pageable pageable);
    Page<PackageRequest> findAllByOrderIdAndCustomerIdAndPackageStatus(String orderId, String customerId, PackageStatus packageStatus, Pageable pageable);
    Page<PackageRequest> findAllByOrderIdAndCustomerId(String orderId, String customerId, Pageable pageable);
    Page<PackageRequest> findAllByOrderIdAndSellerIdAndPackageStatus(String orderId, String sellerId, PackageStatus packageStatus, Pageable pageable);
    Page<PackageRequest> findAllByOrderIdAndSellerId(String orderId, String sellerId, Pageable pageable);
    Page<PackageRequest> findAllByOrderIdAndPackageStatus(String orderId, PackageStatus packageStatus, Pageable pageable);
    Page<PackageRequest> findAllByOrderId(String orderId, Pageable pageable);
    Page<PackageRequest> findAllByCustomerIdAndSellerIdAndPackageStatus(String customerId, String sellerId, PackageStatus packageStatus, Pageable pageable);
    Page<PackageRequest> findAllByCustomerIdAndSellerId(String customerId, String sellerId, Pageable pageable);
    Page<PackageRequest> findAllByCustomerIdAndPackageStatus(String customerId, PackageStatus packageStatus, Pageable pageable);
    Page<PackageRequest> findAllByCustomerId(String customerId, Pageable pageable);
    Page<PackageRequest> findAllBySellerIdAndPackageStatus(String sellerId, PackageStatus packageStatus, Pageable pageable);
    Page<PackageRequest> findAllBySellerId(String sellerId, Pageable pageable);
    Page<PackageRequest> findAllByPackageStatus(PackageStatus packageStatus, Pageable pageable);
    Optional<PackageRequest> findByIdAndCustomerIdOrSellerId(String packageRequestId, String customerId, String sellerId);
}
