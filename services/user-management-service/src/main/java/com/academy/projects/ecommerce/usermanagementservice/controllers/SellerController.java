package com.academy.projects.ecommerce.usermanagementservice.controllers;

import com.academy.projects.ecommerce.usermanagementservice.dtos.SellerRegistrationDto;
import com.academy.projects.ecommerce.usermanagementservice.dtos.UserRegistrationResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.models.Address;
import com.academy.projects.ecommerce.usermanagementservice.models.Seller;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import com.academy.projects.ecommerce.usermanagementservice.services.usermanagement.ISellerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/users/sellers")
public class SellerController {
    private final ISellerManagementService sellerManagementService;

    @Autowired
    public SellerController(final ISellerManagementService sellerManagementService) {
        this.sellerManagementService = sellerManagementService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto> register(@RequestBody SellerRegistrationDto sellerRegistrationDto) {
        UserRegistrationResponseDto responseDto = new UserRegistrationResponseDto();
        Seller seller = sellerManagementService.register(sellerRegistrationDto);
        responseDto.setMessage("Seller registered successfully!!! Check the Approval Tracking for Approval Details!!!");
        responseDto.setUserId(seller.getId());
        responseDto.setCreatedOrModifiedAt(seller.getCreatedAt());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('ROLE_SELLER') and @accessChecker.isOwner(#seller.id)")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Seller updateSeller(@RequestBody Seller seller) {
        return sellerManagementService.update(seller);
    }

    @PreAuthorize("hasAnyAuthority('CRUD_USER', 'CRUD_SELLER') or hasRole('ROLE_SELLER_MANAGER')")
    @GetMapping("/{userId}")
    public ResponseEntity<Seller> getSeller(@PathVariable String userId) {
        Seller seller = sellerManagementService.getSeller(userId);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<Seller> getSeller(Authentication authentication) {
        Seller seller = sellerManagementService.getSeller(authentication.getName());
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CRUD_USER', 'CRUD_SELLER') or hasRole('ROLE_SELLER_MANAGER')")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Seller> getAllSellers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) UserState userState) {
        return sellerManagementService.getSellers(page, pageSize, userState);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('CRUD_USER', 'CRUD_SELLER') or hasRole('ROLE_SELLER_MANAGER')")
    public Seller updateState(@PathVariable String userId, @RequestBody UserState userState, Authentication authentication) {
        return sellerManagementService.updateState(userId, userState, authentication.getName());
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @PostMapping("/addresses")
    public ResponseEntity<Seller> addAddress(Authentication authentication, @RequestBody Address address) {
        Seller seller = sellerManagementService.addAddress(authentication.getName(), address);
        return new ResponseEntity<>(seller, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @PatchMapping("/addresses")
    public ResponseEntity<Seller> updateAddress(Authentication authentication, @RequestBody Address address) {
        Seller seller = sellerManagementService.updateAddress(authentication.getName(), address);
        return new ResponseEntity<>(seller, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @GetMapping("/addresses")
    public ResponseEntity<Address> getAddress(Authentication authentication) {
        Address address = sellerManagementService.getAddress(authentication.getName());
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{sellerId}/addresses")
    public ResponseEntity<Address> getAddress(@PathVariable String sellerId) {
        Address address = sellerManagementService.getAddress(sellerId);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }
}
