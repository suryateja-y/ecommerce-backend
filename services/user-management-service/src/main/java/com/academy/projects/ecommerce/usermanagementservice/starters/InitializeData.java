package com.academy.projects.ecommerce.usermanagementservice.starters;

import com.academy.projects.ecommerce.usermanagementservice.models.*;
import com.academy.projects.ecommerce.usermanagementservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings("NullableProblems")
@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private boolean alreadySetup = false;

    private final EmployeeRepository employeeRepository;
    private final SellerRepository sellerRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public InitializeData(EmployeeRepository employeeRepository, SellerRepository sellerRepository, CustomerRepository customerRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.employeeRepository = employeeRepository;
        this.sellerRepository = sellerRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;

        employeeRepository.deleteAll();
        sellerRepository.deleteAll();
        customerRepository.deleteAll();
        userRepository.deleteAll();

        // Creating ADMIN user
        Employee admin = new Employee();

        User adminUser = new User();
        adminUser.setId(GlobalData.ADMIN_ID);
        adminUser.setEmail(GlobalData.ADMIN_EMAIL);
        adminUser.setUserType(UserType.EMPLOYEE);
        adminUser.setUserState(UserState.APPROVED);
        adminUser.setFullName("Academy");
        adminUser.setPhoneNumber("+919999999999");
        adminUser = userRepository.save(adminUser);

        admin.setUser(adminUser);
        admin.setId(adminUser.getId());
        admin.setAge(30);
        admin.setBloodGroup("O+");
        admin.setGender(Gender.MALE);
        admin.setDesignation("CEO");
        admin.setEmployeeId("Academy-000");
        employeeRepository.save(admin);

        // Creating Seller
        Seller seller = new Seller();
        User sellerUser = new User();
        sellerUser.setId(GlobalData.SELLER_ID);
        sellerUser.setEmail(GlobalData.SELLER_EMAIL);
        sellerUser.setUserType(UserType.SELLER);
        sellerUser.setUserState(UserState.APPROVED);
        sellerUser.setFullName("Academy");
        sellerUser.setPhoneNumber("+919999999999");
        sellerUser = userRepository.save(sellerUser);

        seller.setUser(sellerUser);
        seller.setId(sellerUser.getId());
        seller.setBrandName("Academy");
        seller.setCompanyName("Academy Corporation Private Limited");

        Address address = new Address();
        address.setHouseNumber("SH-1");
        address.setStreet("Seller Galaxy");
        address.setAddressLine1("Galaxy Street");
        address.setCity("Star City");
        address.setState("Milky Way");
        address.setCountry("India");
        address.setZip("666666");
        address = addressRepository.save(address);

        seller.setAddress(address);
        sellerRepository.save(seller);

        Seller anotherSeller = new Seller();
        User anotherSellerUser = new User();
        anotherSellerUser.setId(GlobalData.SELLER2_ID);
        anotherSellerUser.setEmail(GlobalData.SELLER_EMAIL);
        anotherSellerUser.setUserType(UserType.SELLER);
        anotherSellerUser.setUserState(UserState.APPROVED);
        anotherSellerUser.setFullName("Academy");
        anotherSellerUser.setPhoneNumber("+919999999999");
        anotherSellerUser = userRepository.save(anotherSellerUser);

        anotherSeller.setUser(anotherSellerUser);
        anotherSeller.setId(anotherSellerUser.getId());
        anotherSeller.setBrandName("Academy");
        anotherSeller.setCompanyName("Academy Corporation Private Limited");

        Address anotherSellerAddress = new Address();
        anotherSellerAddress.setHouseNumber("SH-2");
        anotherSellerAddress.setStreet("Seller Galaxy");
        anotherSellerAddress.setAddressLine1("Galaxy Street");
        anotherSellerAddress.setCity("Star City");
        anotherSellerAddress.setState("Milky Way");
        anotherSellerAddress.setCountry("India");
        anotherSellerAddress.setZip("666666");
        anotherSellerAddress = addressRepository.save(anotherSellerAddress);

        anotherSeller.setAddress(anotherSellerAddress);
        sellerRepository.save(anotherSeller);

        // Creating Customer
        Customer customer = new Customer();
        User customerUser = new User();
        customerUser.setId(GlobalData.CUSTOMER_ID);
        customerUser.setEmail(GlobalData.CUSTOMER_EMAIL);
        customerUser.setUserType(UserType.CUSTOMER);
        customerUser.setUserState(UserState.APPROVED);
        customerUser.setFullName("Academy");
        customerUser.setPhoneNumber("+919999999999");
        customerUser = userRepository.save(customerUser);

        customer.setUser(customerUser);
        customer.setId(customerUser.getId());
        customer.setAge(30);
        customer.setGender(Gender.MALE);
        customerRepository.save(customer);

        // Creating Employee
        Employee employee = new Employee();
        User employeeUser = new User();
        employeeUser.setId(GlobalData.EMPLOYEE_ID);
        employeeUser.setEmail(GlobalData.EMPLOYEE_EMAIL);
        employeeUser.setUserType(UserType.EMPLOYEE);
        employeeUser.setUserState(UserState.APPROVED);
        employeeUser.setFullName("Academy");
        employeeUser.setPhoneNumber("+919999999999");
        employeeUser = userRepository.save(employeeUser);

        employee.setUser(employeeUser);
        employee.setId(employeeUser.getId());
        employee.setAge(30);
        employee.setBloodGroup("O+");
        employee.setGender(Gender.MALE);
        employee.setDesignation("AI Scientist");
        employee.setEmployeeId("Emp-01");
        employeeRepository.save(employee);

        // Seller Manager
        Employee sellerManager = new Employee();
        User sellerManagerUser = new User();
        sellerManagerUser.setId(GlobalData.SELLER_MANAGER_ID);
        sellerManagerUser.setEmail(GlobalData.SELLER_MANAGER_EMAIL);
        sellerManagerUser.setUserType(UserType.EMPLOYEE);
        sellerManagerUser.setUserState(UserState.APPROVED);
        sellerManagerUser.setFullName("Academy");
        sellerManagerUser.setPhoneNumber("+919999999999");
        sellerManagerUser = userRepository.save(sellerManagerUser);

        sellerManager.setUser(sellerManagerUser);
        sellerManager.setId(sellerManagerUser.getId());
        sellerManager.setAge(30);
        sellerManager.setBloodGroup("O+");
        sellerManager.setGender(Gender.MALE);
        sellerManager.setDesignation("Seller Manager");
        sellerManager.setEmployeeId("Emp-02");
        employeeRepository.save(sellerManager);

        Employee productManager = new Employee();
        User productManagerUser = new User();
        productManagerUser.setId(GlobalData.PRODUCT_MANAGER_ID);
        productManagerUser.setEmail(GlobalData.PRODUCT_MANAGER_EMAIL);
        productManagerUser.setUserType(UserType.EMPLOYEE);
        productManagerUser.setUserState(UserState.APPROVED);
        productManagerUser.setFullName("Academy");
        productManagerUser.setPhoneNumber("+919999999999");
        productManagerUser = userRepository.save(productManagerUser);

        productManager.setUser(productManagerUser);
        productManager.setId(productManagerUser.getId());
        productManager.setAge(30);
        productManager.setBloodGroup("O+");
        productManager.setGender(Gender.MALE);
        productManager.setDesignation("Product Manager");
        productManager.setEmployeeId("Emp-03");
        employeeRepository.save(productManager);

        alreadySetup = true;
    }
}
