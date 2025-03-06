package com.academy.projects.ecommerce.usermanagementservice.controllers;

import com.academy.projects.ecommerce.usermanagementservice.dtos.CustomerRegistrationDto;
import com.academy.projects.ecommerce.usermanagementservice.dtos.UserRegistrationResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.models.Address;
import com.academy.projects.ecommerce.usermanagementservice.models.Customer;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import com.academy.projects.ecommerce.usermanagementservice.services.usermanagement.ICustomerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/users/customers")
public class CustomerController {

    private final ICustomerManagementService customerManagement;

    @Autowired
    public CustomerController(final ICustomerManagementService customerManagement) {
        this.customerManagement = customerManagement;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto> register(@RequestBody CustomerRegistrationDto customerDto) {
        UserRegistrationResponseDto responseDto = new UserRegistrationResponseDto();
        Customer customer = customerManagement.register(customerDto);
        responseDto.setMessage("Customer registered successfully!!!");
        responseDto.setUserId(customer.getId());
        responseDto.setCreatedOrModifiedAt(customer.getCreatedAt());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') and @accessChecker.isOwner(#customer.id)")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerManagement.update(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<Customer> getCustomer(Authentication authentication) {
        Customer customer = customerManagement.getCustomer(authentication.getName());
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CRUD_USER', 'CRUD_CUSTOMER')")
    @GetMapping("/{userId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String userId) {
        Customer customer = customerManagement.getCustomer(userId);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('CRUD_USER', 'CRUD_CUSTOMER')")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Customer> getAllCustomers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) UserState userState) {
        return customerManagement.getCustomers(page, pageSize, userState);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping("/addresses")
    public ResponseEntity<Customer> addAddress(@RequestBody Address address, Authentication authentication) {
        Customer customer = customerManagement.addAddress(authentication.getName(), address);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PatchMapping("/addresses")
    public ResponseEntity<Customer> updateAddress(@RequestBody Address address, Authentication authentication) {
        Customer customer = customerManagement.updateAddress(authentication.getName(), address);
        return new ResponseEntity<>(customer, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<Address> getAddresses(Authentication authentication, @PathVariable String addressId) {
        Address address = customerManagement.getAddress(authentication.getName(), addressId);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRACKING_MANAGER')")
    @GetMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<Address> getCustomerAddress(@PathVariable String customerId, @PathVariable String addressId) {
        Address address = customerManagement.getAddress(customerId, addressId);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getAddresses(Authentication authentication) {
        List<Address> addresses = customerManagement.getAddresses(authentication.getName());
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Customer> removeAddress(@PathVariable String addressId, Authentication authentication) {
        Customer customer = customerManagement.removeAddress(authentication.getName(), addressId);
        return new ResponseEntity<>(customer, HttpStatus.ACCEPTED);
    }
}
