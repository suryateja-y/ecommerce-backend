package com.academy.projects.ecommerce.usermanagementservice.starters;

import com.academy.projects.ecommerce.usermanagementservice.models.*;
import com.academy.projects.ecommerce.usermanagementservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@SuppressWarnings({"NullableProblems", "DuplicatedCode"})
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

        // Clear all repositories
        employeeRepository.deleteAll();
        sellerRepository.deleteAll();
        customerRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();

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
        admin.setEmployeeId(GlobalData.ADMIN_EMPLOYEE_ID);
        employeeRepository.save(admin);

        // Creating Seller
        // Seller 1
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
        address.setZip("523372");
        address = addressRepository.save(address);

        seller.setAddress(address);
        sellerRepository.save(seller);

        // Seller 2
        Seller seller2 = new Seller();
        User seller2User = new User();
        seller2User.setId(GlobalData.SELLER2_ID);
        seller2User.setEmail(GlobalData.SELLER2_EMAIL);
        seller2User.setUserType(UserType.SELLER);
        seller2User.setUserState(UserState.APPROVED);
        seller2User.setFullName("Academy");
        seller2User.setPhoneNumber("+919999999999");
        seller2User = userRepository.save(seller2User);

        seller2.setUser(seller2User);
        seller2.setId(seller2User.getId());
        seller2.setBrandName("Academy");
        seller2.setCompanyName("Academy Corporation Private Limited");

        Address seller2Address = new Address();
        seller2Address.setHouseNumber("SH-2");
        seller2Address.setStreet("Seller Galaxy");
        seller2Address.setAddressLine1("Galaxy Street");
        seller2Address.setCity("Star City");
        seller2Address.setState("Milky Way");
        seller2Address.setCountry("India");
        seller2Address.setZip("523635");
        seller2Address = addressRepository.save(seller2Address);

        seller2.setAddress(seller2Address);
        sellerRepository.save(seller2);

        // Seller 3
        Seller seller3 = new Seller();
        User seller3User = new User();
        seller3User.setId(GlobalData.SELLER3_ID);
        seller3User.setEmail(GlobalData.SELLER3_EMAIL);
        seller3User.setUserType(UserType.SELLER);
        seller3User.setUserState(UserState.APPROVED);
        seller3User.setFullName("Academy");
        seller3User.setPhoneNumber("+919999999999");
        seller3User = userRepository.save(seller3User);

        seller3.setUser(seller3User);
        seller3.setId(seller3User.getId());
        seller3.setBrandName("Academy");
        seller3.setCompanyName("Academy Corporation Private Limited");

        Address seller3Address = new Address();
        seller3Address.setHouseNumber("SH-2");
        seller3Address.setStreet("Seller Galaxy");
        seller3Address.setAddressLine1("Galaxy Street");
        seller3Address.setCity("Star City");
        seller3Address.setState("Milky Way");
        seller3Address.setCountry("India");
        seller3Address.setZip("523754");
        seller3Address = addressRepository.save(seller3Address);

        seller3.setAddress(seller3Address);
        sellerRepository.save(seller3);

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
        employee.setDesignation("Automation Engineer");
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

        // Product Manager
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

        // Inventory Manager
        Employee inventoryManager = new Employee();
        User inventoryManagerUser = new User();
        inventoryManagerUser.setId(GlobalData.INVENTORY_MANAGER_ID);
        inventoryManagerUser.setEmail(GlobalData.INVENTORY_MANAGER_EMAIL);
        inventoryManagerUser.setUserType(UserType.EMPLOYEE);
        inventoryManagerUser.setUserState(UserState.APPROVED);
        inventoryManagerUser.setFullName("Academy");
        inventoryManagerUser.setPhoneNumber("+919999999999");
        inventoryManagerUser = userRepository.save(inventoryManagerUser);

        inventoryManager.setUser(inventoryManagerUser);
        inventoryManager.setId(inventoryManagerUser.getId());
        inventoryManager.setAge(30);
        inventoryManager.setBloodGroup("O+");
        inventoryManager.setGender(Gender.FEMALE);
        inventoryManager.setDesignation("Inventory Manager");
        inventoryManager.setEmployeeId("Emp-04");
        employeeRepository.save(inventoryManager);

        // HR Manager
        Employee hrManager = new Employee();
        User hrManagerUser = new User();
        hrManagerUser.setId(GlobalData.HR_MANAGER_ID);
        hrManagerUser.setEmail(GlobalData.HR_MANAGER_EMAIL);
        hrManagerUser.setUserType(UserType.EMPLOYEE);
        hrManagerUser.setUserState(UserState.APPROVED);
        hrManagerUser.setFullName("Academy");
        hrManagerUser.setPhoneNumber("+919999999999");
        hrManagerUser = userRepository.save(hrManagerUser);

        hrManager.setUser(hrManagerUser);
        hrManager.setId(hrManagerUser.getId());
        hrManager.setAge(30);
        hrManager.setBloodGroup("O+");
        hrManager.setGender(Gender.FEMALE);
        hrManager.setDesignation("HR Manager");
        hrManager.setEmployeeId("Emp-05");
        employeeRepository.save(hrManager);

        // Customer Manager
        Employee customerManager = new Employee();
        User customerManagerUser = new User();
        customerManagerUser.setId(GlobalData.CUSTOMER_MANAGER_ID);
        customerManagerUser.setEmail(GlobalData.CUSTOMER_MANAGER_EMAIL);
        customerManagerUser.setUserType(UserType.EMPLOYEE);
        customerManagerUser.setUserState(UserState.APPROVED);
        customerManagerUser.setFullName("Academy");
        customerManagerUser.setPhoneNumber("+919999999999");
        customerManagerUser = userRepository.save(customerManagerUser);

        customerManager.setUser(customerManagerUser);
        customerManager.setId(customerManagerUser.getId());
        customerManager.setAge(30);
        customerManager.setBloodGroup("O+");
        customerManager.setGender(Gender.MALE);
        customerManager.setDesignation("Customer Manager");
        customerManager.setEmployeeId("Emp-06");
        employeeRepository.save(customerManager);

        // Logistics Executive
        Employee logisticsExecutive = new Employee();
        User logisticsExecutiveUser = new User();
        logisticsExecutiveUser.setId(GlobalData.LOGISTICS_EXECUTIVE_ID);
        logisticsExecutiveUser.setEmail(GlobalData.LOGISTICS_EXECUTIVE_EMAIL);
        logisticsExecutiveUser.setUserType(UserType.EMPLOYEE);
        logisticsExecutiveUser.setUserState(UserState.APPROVED);
        logisticsExecutiveUser.setFullName("Academy");
        logisticsExecutiveUser.setPhoneNumber("+919999999999");
        logisticsExecutiveUser = userRepository.save(logisticsExecutiveUser);

        logisticsExecutive.setUser(logisticsExecutiveUser);
        logisticsExecutive.setId(logisticsExecutiveUser.getId());
        logisticsExecutive.setAge(30);
        logisticsExecutive.setBloodGroup("O+");
        logisticsExecutive.setGender(Gender.MALE);
        logisticsExecutive.setDesignation("Logistics Executive");
        logisticsExecutive.setEmployeeId("Emp-07");
        employeeRepository.save(logisticsExecutive);

        // Tracking Manager
        Employee trackingManager = new Employee();
        User trackingManagerUser = new User();
        trackingManagerUser.setId(GlobalData.TRACKING_MANAGER_ID);
        trackingManagerUser.setEmail(GlobalData.TRACKING_MANAGER_EMAIL);
        trackingManagerUser.setUserType(UserType.EMPLOYEE);
        trackingManagerUser.setUserState(UserState.APPROVED);
        trackingManagerUser.setFullName("Academy");
        trackingManagerUser.setPhoneNumber("+919999999999");
        trackingManagerUser = userRepository.save(trackingManagerUser);

        trackingManager.setUser(trackingManagerUser);
        trackingManager.setId(trackingManagerUser.getId());
        trackingManager.setAge(30);
        trackingManager.setBloodGroup("O+");
        trackingManager.setGender(Gender.MALE);
        trackingManager.setDesignation("Tracking Manager");
        trackingManager.setEmployeeId("Emp-08");
        employeeRepository.save(trackingManager);

        alreadySetup = true;
    }
}
