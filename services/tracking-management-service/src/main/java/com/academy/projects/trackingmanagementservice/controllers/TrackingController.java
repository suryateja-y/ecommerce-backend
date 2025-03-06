package com.academy.projects.trackingmanagementservice.controllers;

import com.academy.projects.trackingmanagementservice.kafka.dtos.ActionDto;
import com.academy.projects.trackingmanagementservice.models.OrderPackage;
import com.academy.projects.trackingmanagementservice.models.PackageRequest;
import com.academy.projects.trackingmanagementservice.models.TrackingStatus;
import com.academy.projects.trackingmanagementservice.services.IPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/tracking/tracking")
public class TrackingController {

    private final IPackageService packageService;

    @Autowired
    public TrackingController(IPackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_TRACKING_MANAGER')")
    public ResponseEntity<List<OrderPackage>> getPackages(@RequestParam(required = false) String trackingNumber, @RequestParam(required = false) String orderId, @RequestParam(required = false) TrackingStatus trackingStatus, @RequestParam(required = false) Date from, @RequestParam(required = false) Date to, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<OrderPackage> orderPackages = packageService.get(trackingNumber, orderId, trackingStatus, from, to, page, pageSize);
        return ResponseEntity.ok(orderPackages);
    }

    @GetMapping("/{trackingNumber}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<OrderPackage> getPackage(@PathVariable String trackingNumber, Authentication authentication) {
        OrderPackage orderPackage = packageService.get(trackingNumber, authentication.getName());
        return new ResponseEntity<>(orderPackage, HttpStatus.OK);
    }

    @PatchMapping("/{trackingNumber}")
    @PreAuthorize("hasAnyRole('ROLE_LOGISTIC_EXECUTIVE', 'ROLE_TRACKING_MANAGER')")
    public ResponseEntity<OrderPackage> updatePackage(@RequestBody ActionDto actionDto, @PathVariable String trackingNumber) {
        OrderPackage orderPackage = packageService.update(trackingNumber, actionDto.getDestination(), actionDto.getTrackingStatus(), actionDto.getMessage());
        return new ResponseEntity<>(orderPackage, HttpStatus.ACCEPTED);
    }

}
