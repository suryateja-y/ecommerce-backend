package com.academy.projects.ecommerce.usermanagementservice.services.usermanagement;

import com.academy.projects.ecommerce.usermanagementservice.dtos.CustomerRegistrationDto;
import com.academy.projects.ecommerce.usermanagementservice.models.Address;
import com.academy.projects.ecommerce.usermanagementservice.models.Customer;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;

import java.util.List;

public interface ICustomerManagementService {
    Customer register(CustomerRegistrationDto customerDto);
    Customer update(Customer customer);
    Customer getCustomer(String userId);
    List<Customer> getCustomers(int page, int pageSize, UserState userState);
    Customer addAddress(String userId, Address address);
    Customer updateAddress(String userId, Address address);
    List<Address> getAddresses(String userId);
    Customer removeAddress(String userId, String addressId);
    Address getAddress(String userId, String addressId);
}
