package com.academy.projects.trackingmanagementservice.controllers;

import com.academy.projects.trackingmanagementservice.models.OrderPackage;
import com.academy.projects.trackingmanagementservice.models.PackageRequest;
import com.academy.projects.trackingmanagementservice.models.PackageStatus;
import com.academy.projects.trackingmanagementservice.services.PackageRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/tracking/package-requests")
public class PackageRequestController {

    private final PackageRequestService packageRequestService;

    public PackageRequestController(PackageRequestService packageRequestService) {
        this.packageRequestService = packageRequestService;
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_TRACKING_MANAGER')")
    public ResponseEntity<List<PackageRequest>> getPackageRequests(@RequestParam(required = false) String packageRequestId, @RequestParam(required = false) String orderId, @RequestParam(required = false) String customerId, @RequestParam(required = false) String sellerId, @RequestParam(required = false) PackageStatus packageStatus, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<PackageRequest> packageRequests = packageRequestService.get(packageRequestId, orderId, customerId, sellerId, packageStatus, page, pageSize);
        return ResponseEntity.ok(packageRequests);
    }

    @PostMapping("/convert")
    @PreAuthorize("hasAnyRole('ROLE_TRACKING_MANAGER')")
    public ResponseEntity<OrderPackage> convertPackageRequest(@RequestParam String packageRequestId) {
        OrderPackage orderPackage = packageRequestService.convert(packageRequestId);
        return new ResponseEntity<>(orderPackage, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{packageRequestId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<PackageRequest> getPackageRequest(@PathVariable String packageRequestId, Authentication authentication) {
        PackageRequest packageRequest = packageRequestService.get(packageRequestId, authentication.getName());
        return new ResponseEntity<>(packageRequest, HttpStatus.OK);
    }
}
