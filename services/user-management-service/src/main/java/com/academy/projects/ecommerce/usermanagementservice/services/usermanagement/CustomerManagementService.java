package com.academy.projects.ecommerce.usermanagementservice.services.usermanagement;

import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.SignUpRequestDto;
import com.academy.projects.ecommerce.usermanagementservice.configurations.Patcher;
import com.academy.projects.ecommerce.usermanagementservice.dtos.CustomerRegistrationDto;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.AddressNotFoundException;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.IdNotProvidedException;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.UserAlreadyExistsException;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.UserNotFoundException;
import com.academy.projects.ecommerce.usermanagementservice.models.*;
import com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user.ICustomerUpdateManager;
import com.academy.projects.ecommerce.usermanagementservice.repositories.AddressRepository;
import com.academy.projects.ecommerce.usermanagementservice.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerManagementService implements ICustomerManagementService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final IUserManagementService userManagementService;
    private final ICustomerUpdateManager customerUpdateManager;
    private final Patcher patcher;
    private final PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(CustomerManagementService.class);

    @Autowired
    public CustomerManagementService(CustomerRepository customerRepository, AddressRepository addressRepository, IUserManagementService userManagementService, ICustomerUpdateManager customerUpdateManager, Patcher patcher, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.userManagementService = userManagementService;
        this.customerUpdateManager = customerUpdateManager;
        this.patcher = patcher;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer register(CustomerRegistrationDto customerDto) {
        if(userManagementService.userExists(customerDto.getEmail(), UserType.CUSTOMER)) throw new UserAlreadyExistsException(customerDto.getEmail(), UserType.CUSTOMER);

        // Registering Customer into the Authentication Service
        String userId = userManagementService.registerInAuthenticationService(SignUpRequestDto.builder()
                .email(customerDto.getEmail())
                .password(passwordEncoder.encode(customerDto.getPassword()))
                .userType(UserType.CUSTOMER)
                .build());

        User user = new User();
        user.setEmail(customerDto.getEmail());
        user.setFullName(customerDto.getFullName());
        user.setUserType(UserType.CUSTOMER);
        user.setUserState(UserState.APPROVED);
        user.setPhoneNumber(customerDto.getPhoneNumber());
        user.setId(userId);
        User newUser = userManagementService.save(user);

        Customer customer = new Customer();
        customer.setAge(customerDto.getAge());
        customer.setGender(customerDto.getGender());
        customer.setUser(newUser);
        customer.setId(userId);

        customer = customerRepository.save(customer);
        logger.info("Customer: '{}' has been registered successfully!!!", customer.getUser().getEmail());
        // Send confirmation Email and Notification
        customerUpdateManager.sendRegistration(customer);
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        if(customer.getId() == null) throw new IdNotProvidedException();
        Customer savedCustomer = customerRepository.findById(customer.getId()).orElseThrow(() -> new UserNotFoundException(customer.getId()));
        User savedUser = savedCustomer.getUser();
        patcher.entity(savedUser, customer.getUser(), User.class);
        patcher.entity(savedCustomer, customer, Customer.class);
        savedUser.setId(savedCustomer.getId());
        savedUser = userManagementService.save(savedUser);
        savedCustomer.setUser(savedUser);
        savedCustomer = customerRepository.save(savedCustomer);
        logger.info("Customer: '{}' has been updated successfully!!!", customer.getUser().getEmail());
        customerUpdateManager.sendUpdate(customer);
        return savedCustomer;
    }

    @Override
    public Customer getCustomer(String userId) {
        return customerRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<Customer> getCustomers(int page, int pageSize, UserState userState) {
        if(userState != null) return this.getCustomersByUserState(page, pageSize, userState);
        Pageable pageable = PageRequest.of(page, pageSize);
        return customerRepository.findAll(pageable).getContent();
    }

    @Override
    public Customer addAddress(String userId, Address address) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        customer.getAddresses().add(address);
        customer = customerRepository.save(customer);
        logger.info("Address: '{}' has been added successfully to the customer: '{}'!!!", address.getId(), customer.getUser().getEmail());
        return customer;
    }

    @Override
    public Customer updateAddress(String userId, Address address) {
        if(address.getId() == null) throw new IdNotProvidedException();
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        for(Address savedAddress : customer.getAddresses()) {
            if(savedAddress.getId().equals(address.getId())) {
                savedAddress.setCity(address.getCity());
                savedAddress.setCountry(address.getCountry());
                savedAddress.setState(address.getState());
                savedAddress.setZip(address.getZip());
                savedAddress.setStreet(address.getStreet());
                savedAddress.setHouseNumber(address.getHouseNumber());
                logger.info("Address: '{}' has been updated successfully!!!", address.getId());
                return customerRepository.save(customer);
            }
        }
        throw new AddressNotFoundException(userId, address.getId());
    }

    @Override
    public List<Address> getAddresses(String userId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return customer.getAddresses();
    }

    @Override
    public Customer removeAddress(String userId, String addressId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException(userId, addressId));
        customer.getAddresses().remove(address);
        customer = customerRepository.save(customer);
        logger.info("Address: '{}' has been deleted successfully from the Customer: '{}'!!!", addressId, customer.getUser().getEmail());
        return customer;
    }

    @Override
    public Address getAddress(String userId, String addressId) {
        Customer customer = customerRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        for(Address savedAddress : customer.getAddresses()) {
            if(savedAddress.getId().equals(addressId)) return savedAddress;
        }
        throw new AddressNotFoundException(userId, addressId);
    }

    private List<Customer> getCustomersByUserState(int page, int pageSize, UserState userState) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Customer> customerPage = customerRepository.findAllByUser_UserState(userState, pageable);
        return customerPage.getContent();
    }

}
