package com.academy.projects.trackingmanagementservice.services;

import com.academy.projects.trackingmanagementservice.clients.services.UserManagementServiceClient;
import com.academy.projects.trackingmanagementservice.dtos.ActionStatus;
import com.academy.projects.trackingmanagementservice.dtos.CancelResponseDto;
import com.academy.projects.trackingmanagementservice.exceptions.CustomerOrAddressNotFoundException;
import com.academy.projects.trackingmanagementservice.exceptions.PackageNotFoundException;
import com.academy.projects.trackingmanagementservice.exceptions.SellerOrAddressNotFoundException;
import com.academy.projects.trackingmanagementservice.kafka.dtos.Action;
import com.academy.projects.trackingmanagementservice.kafka.producers.services.ITrackingUpdateManager;
import com.academy.projects.trackingmanagementservice.models.*;
import com.academy.projects.trackingmanagementservice.repositories.PackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PackageService implements IPackageService {
    private final PackageRepository packageRepository;
    private final ITrackingNumberService trackingNumberService;
    private final UserManagementServiceClient userManagementServiceClient;
    private final ITrackingUpdateManager trackingUpdateManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PackageService(PackageRepository packageRepository, ITrackingNumberService trackingNumberService, UserManagementServiceClient userManagementServiceClient, ITrackingUpdateManager trackingUpdateManager) {
        this.packageRepository = packageRepository;
        this.trackingNumberService = trackingNumberService;
        this.userManagementServiceClient = userManagementServiceClient;
        this.trackingUpdateManager = trackingUpdateManager;
    }

    @Override
    public CancelResponseDto cancel(String orderId) {
        OrderPackage orderPackage = packageRepository.findById(orderId).orElse(null);
        if (orderPackage != null) {
            if(!orderPackage.getTrackingStatus().equals(TrackingStatus.DELIVERED)) {
                orderPackage.setTrackingStatus(TrackingStatus.RETURNED);
                packageRepository.save(orderPackage);
                logger.info("Order Cancelled successfully. Order Id: '{}'!!!", orderId);
                return CancelResponseDto.builder().responseStatus(ActionStatus.SUCCEEDED).orderPackage(orderPackage).build();
            } else {
                logger.info("Failed to cancel the order because the order has already been delivered. Order Id: {}'!!!", orderId);
                return CancelResponseDto.builder().responseStatus(ActionStatus.FAILED).orderPackage((orderPackage)).responseMessage("Order Package has already been delivered!!!").build();
            }
        }
        return CancelResponseDto.builder().responseStatus(ActionStatus.FAILED).orderPackage(null).build();
    }

    @Override
    public OrderPackage convert(PackageRequest packageRequest) {
        Address customerAddress = this.getCustomerAddress(packageRequest.getCustomerId(), packageRequest.getDeliveryAddressId());
        Address sellerAddress = this.getSellerAddress(packageRequest.getSellerId());

        OrderPackage orderPackage = new OrderPackage();
        orderPackage.setTrackingStatus(TrackingStatus.CREATED);
        orderPackage.setOrderId(packageRequest.getOrderId());
        orderPackage.setActions(List.of());
        orderPackage.setEta(packageRequest.getEta());
        orderPackage.setTrackingNumber(trackingNumberService.getTrackingNumber());
        orderPackage.setDeliveryAddress(customerAddress);
        orderPackage.setSellerAddress(sellerAddress);
        orderPackage.setCustomerId(packageRequest.getCustomerId());
        orderPackage = packageRepository.save(orderPackage);
        // Send update to Kafka
        trackingUpdateManager.sendUpdate(orderPackage, Action.CREATE);
        return orderPackage;
    }

    @Override
    public List<OrderPackage> get(String trackingNumber, String orderId, TrackingStatus trackingStatus, Date from, Date to, int page, int pageSize) {
        if(trackingNumber != null && !trackingNumber.isEmpty()) {
            OrderPackage orderPackage = packageRepository.findByTrackingNumberOrId(trackingNumber, trackingNumber).orElse(null);
            return (orderPackage != null) ? List.of(orderPackage) : List.of();
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<OrderPackage> orderPackagePage;

        if(orderId != null && !orderId.isEmpty()) {
            if(trackingStatus != null) {
                if(from != null && to != null) orderPackagePage = packageRepository.findAllByOrderIdAndTrackingStatusAndEtaBetween(orderId, trackingStatus, from, to, pageable);
                else orderPackagePage = packageRepository.findAllByOrderIdAndTrackingStatus(orderId, trackingStatus, pageable);
            } else {
                if(from != null && to != null) orderPackagePage = packageRepository.findAllByOrderIdAndEtaBetween(orderId, from, to, pageable);
                else orderPackagePage = packageRepository.findAllByOrderId(orderId, pageable);
            }
        } else {
            if(trackingStatus != null) {
                if(from != null && to != null) orderPackagePage = packageRepository.findAllByTrackingStatusAndEtaBetween(trackingStatus, from, to, pageable);
                else orderPackagePage = packageRepository.findAllByTrackingStatus(trackingStatus, pageable);
            } else {
                if(from != null && to != null) orderPackagePage = packageRepository.findAllByEtaBetween(from, to, pageable);
                else orderPackagePage = packageRepository.findAll(pageable);
            }
        }
        return (orderPackagePage != null) ? orderPackagePage.getContent() : List.of();
    }

    @Override
    public OrderPackage get(String trackingNumber, String userId) {
        return null;
    }

    @Override
    public OrderPackage update(String trackingNumber, String destination, TrackingStatus trackingStatus, String message) {
        OrderPackage orderPackage = packageRepository.findByTrackingNumberOrId(trackingNumber, trackingNumber).orElseThrow(() -> new PackageNotFoundException(trackingNumber));
        List<TrackingAction> actions = orderPackage.getActions();
        String lastLocation = (actions.isEmpty()) ? "" : actions.get(actions.size() - 1).getDestination();
        TrackingAction trackingAction = TrackingAction.builder()
                .currentLocation(lastLocation)
                .destination(destination)
                .trackingStatus(trackingStatus)
                .actedOn(new Date())
                .message(message).build();
        orderPackage.getActions().add(trackingAction);

        if((trackingStatus == TrackingStatus.OUT_FOR_DELIVERY) || (trackingStatus == TrackingStatus.DELIVERED))
            orderPackage.setTrackingStatus(trackingStatus);
        else
            orderPackage.setTrackingStatus(TrackingStatus.IN_TRANSIT);
        orderPackage = packageRepository.save(orderPackage);
        logger.info("Order updated successfully. Order Id: '{}'!!!", orderPackage.getId());
        // Send update to Kafka
        trackingUpdateManager.sendUpdate(orderPackage, Action.STATUS_UPDATE);
        return orderPackage;
    }

    private Address getCustomerAddress(String customerId, String addressId) {
        try {
            ResponseEntity<Address> customerAddress = userManagementServiceClient.getCustomerAddress(customerId, addressId);
            return customerAddress.getBody();
        } catch(Exception e) {
            // Extract the message
            logger.error(e.getMessage());
            throw new CustomerOrAddressNotFoundException(customerId, addressId);
        }
    }

    private Address getSellerAddress(String sellerId) {
        try {
            ResponseEntity<Address> sellerAddress = userManagementServiceClient.getSellerAddress(sellerId);
            return sellerAddress.getBody();
        } catch(Exception e) {
            // Extract message
            logger.error(e.getMessage());
            throw new SellerOrAddressNotFoundException(sellerId);
        }
    }
}
