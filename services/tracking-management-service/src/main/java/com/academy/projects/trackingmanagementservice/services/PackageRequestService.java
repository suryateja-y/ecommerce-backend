package com.academy.projects.trackingmanagementservice.services;

import com.academy.projects.trackingmanagementservice.dtos.ActionStatus;
import com.academy.projects.trackingmanagementservice.dtos.CancelResponseDto;
import com.academy.projects.trackingmanagementservice.exceptions.PackageAlreadyPackedOrCancelledException;
import com.academy.projects.trackingmanagementservice.exceptions.PackageRequestNotFoundException;
import com.academy.projects.trackingmanagementservice.exceptions.SellerDetailsNotFound;
import com.academy.projects.trackingmanagementservice.models.*;
import com.academy.projects.trackingmanagementservice.repositories.PackageRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageRequestService implements IPackageRequestService {
    private final PackageRequestRepository packageRequestRepository;
    private final IPackageService packageService;

    private final Logger logger = LoggerFactory.getLogger(PackageRequestService.class);

    @Autowired
    public PackageRequestService(PackageRequestRepository packageRequestRepository, IPackageService packageService) {
        this.packageRequestRepository = packageRequestRepository;
        this.packageService = packageService;
    }

    @Override
    public void create(PackageRequest packageRequest) {
        if(packageRequestRepository.existsByOrderId(packageRequest.getOrderId())) return;
        if((packageRequest.getSellerId() == null) || packageRequest.getSellerId().isEmpty()) throw new SellerDetailsNotFound("Seller Id not provided for the order: '" + packageRequest.getOrderId() + "'!!!");
        packageRequest = packageRequestRepository.save(packageRequest);
        logger.info("Package Request created for the order: '{}' successfully!!! Request ID: '{}'!!!", packageRequest.getOrderId(), packageRequest.getId());
    }

    @Override
    public CancelResponseDto cancel(String orderId) {
        CancelResponseDto cancelResponseDto;
        PackageRequest packageRequest = packageRequestRepository.findByOrderId(orderId).orElse(null);
        if(packageRequest != null) {
            packageRequest.setPackageStatus(PackageStatus.CANCELLED);
            packageRequest = packageRequestRepository.save(packageRequest);
            logger.info("Package Request for the order: '{}' has been cancelled!!!s", packageRequest.getOrderId());
            return CancelResponseDto.builder().responseStatus(ActionStatus.SUCCEEDED).build();
        } else {
            cancelResponseDto = packageService.cancel(orderId);
        }
        return cancelResponseDto;
    }

    @Override
    public List<PackageRequest> get(String packageRequestId, String orderId, String customerId, String sellerId, PackageStatus packageStatus, int page, int pageSize) {
        if(packageRequestId != null) {
            PackageRequest packageRequest = packageRequestRepository.findById(packageRequestId).orElse(null);
            return (packageRequest != null) ? List.of(packageRequest) : List.of();
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<PackageRequest> packageRequestPage;
        if(orderId != null && !orderId.isEmpty()) {
            if(customerId != null && !customerId.isEmpty()) {
                if(sellerId != null && !sellerId.isEmpty()) {
                    if(packageStatus != null) packageRequestPage = packageRequestRepository.findAllByOrderIdAndCustomerIdAndSellerIdAndPackageStatus(orderId, customerId, sellerId, packageStatus, pageable);
                    else packageRequestPage = packageRequestRepository.findAllByOrderIdAndCustomerIdAndSellerId(orderId, customerId, sellerId, pageable);
                } else {
                    if(packageStatus != null) packageRequestPage = packageRequestRepository.findAllByOrderIdAndCustomerIdAndPackageStatus(orderId, customerId, packageStatus, pageable);
                    else packageRequestPage = packageRequestRepository.findAllByOrderIdAndCustomerId(orderId, customerId, pageable);
                }
            } else {
                if(sellerId != null && !sellerId.isEmpty()) {
                    if(packageStatus != null) packageRequestPage = packageRequestRepository.findAllByOrderIdAndSellerIdAndPackageStatus(orderId, sellerId, packageStatus, pageable);
                    else packageRequestPage = packageRequestRepository.findAllByOrderIdAndSellerId(orderId, sellerId, pageable);
                } else {
                    if(packageStatus != null) packageRequestPage = packageRequestRepository.findAllByOrderIdAndPackageStatus(orderId, packageStatus, pageable);
                    else packageRequestPage = packageRequestRepository.findAllByOrderId(orderId, pageable);
                }
            }
        } else {
            if(customerId != null && !customerId.isEmpty()) {
                if(sellerId != null && !sellerId.isEmpty()) {
                    if(packageStatus != null) packageRequestPage = packageRequestRepository.findAllByCustomerIdAndSellerIdAndPackageStatus(customerId, sellerId, packageStatus, pageable);
                    else packageRequestPage = packageRequestRepository.findAllByCustomerIdAndSellerId(customerId, sellerId, pageable);
                } else {
                    if(packageStatus != null) packageRequestPage = packageRequestRepository.findAllByCustomerIdAndPackageStatus(customerId, packageStatus, pageable);
                    else packageRequestPage = packageRequestRepository.findAllByCustomerId(customerId, pageable);
                }
            } else {
                if(sellerId != null && !sellerId.isEmpty()) {
                    if(packageStatus != null) packageRequestPage = packageRequestRepository.findAllBySellerIdAndPackageStatus(sellerId, packageStatus, pageable);
                    else packageRequestPage = packageRequestRepository.findAllBySellerId(sellerId, pageable);
                } else {
                    if(packageStatus != null) packageRequestPage = packageRequestRepository.findAllByPackageStatus(packageStatus, pageable);
                    else packageRequestPage = packageRequestRepository.findAll(pageable);
                }
            }
        }

        return packageRequestPage.getContent();
    }

    @Override
    public OrderPackage convert(String packageRequestId) {
        PackageRequest packageRequest = packageRequestRepository.findById(packageRequestId).orElseThrow(() -> new PackageRequestNotFoundException(packageRequestId));
        if(!packageRequest.getPackageStatus().equals(PackageStatus.CREATED)) throw new PackageAlreadyPackedOrCancelledException(packageRequest.getId(), packageRequest.getPackageStatus());

        OrderPackage orderPackage = packageService.convert(packageRequest);

        packageRequest.setPackageStatus(PackageStatus.PACKED);
        packageRequest.setOrderPackage(orderPackage);
        packageRequestRepository.save(packageRequest);
        return orderPackage;
    }

    @Override
    public PackageRequest get(String packageRequestId, String userId) {
        return packageRequestRepository.findByIdAndCustomerIdOrSellerId(packageRequestId, userId, userId).orElseThrow(() -> new PackageRequestNotFoundException(packageRequestId, userId));
    }


}
