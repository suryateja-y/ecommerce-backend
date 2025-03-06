package com.academy.projects.ecommerce.authenticationservice.starters;

import com.academy.projects.ecommerce.authenticationservice.models.*;
import com.academy.projects.ecommerce.authenticationservice.repositories.authentication.TokenRepository;
import com.academy.projects.ecommerce.authenticationservice.repositories.authentication.UserRepository;
import com.academy.projects.ecommerce.authenticationservice.repositories.authorization.PermissionRepository;
import com.academy.projects.ecommerce.authenticationservice.repositories.authorization.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${application.security.user.global.password}")
    private String userPassword;

    private boolean alreadySetup = false;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    public InitializeData(final PasswordEncoder passwordEncoder, final UserRepository userRepository, PermissionRepository permissionRepository, RoleRepository roleRepository, TokenRepository tokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
    }

    @SuppressWarnings({"NullableProblems", "DuplicatedCode"})
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;

        // Clear all repositories
        userRepository.deleteAll();
        permissionRepository.deleteAll();
        roleRepository.deleteAll();
        tokenRepository.deleteAll();

        // Permissions for USER role
        // Creating UPDATE_USER permission
        Permission updateUserPermission = this.createPermission("UPDATE_USER", "This permission allows to update the User Profile");

        // Creating GET_USER permission
        Permission getUserPermission = this.createPermission("GET_USER", "This permission allows to retrieve user's own profile");

        // Creating GET_ROLES permission
        Permission getRolesPermission = this.createPermission("GET_ROLES", "This permission allows to retrieve the user's roles");

        // Permissions for ADMIN role
        // Creating GET_ALL_USERS permission
        Permission getUsersPermission = this.createPermission("GET_ALL_USERS", "This permission allows to retrieve the User Profiles");

        // Creating GET_ALL_ROLES permission
        Permission getAllRolesPermission = this.createPermission("GET_ALL_ROLES", "This permission allows to retrieve all the roles");

        // Creating GET_ALL_PERMISSIONS permission
        Permission getAllPermissions = this.createPermission("GET_ALL_PERMISSIONS", "This permission allows to retrieve all permissions");

        // Creating Get / Add / Update / Delete a role
        Permission crudRolePermission = this.createPermission("CRUD_ROLE", "This permission allows to create, update, delete a role");

        // Creating Get / Add / Update / Delete a permission
        Permission crudPermission = this.createPermission("CRUD_PERMISSION", "This permission allows to create, update, delete a permission");

        // Creating Get / Add / Update / Delete a user
        Permission crudUserPermission = this.createPermission("CRUD_USER", "This permission allows to create, update, delete user");

        // Managing Sellers
        Permission manageSellers = this.createPermission("CRUD_SELLER", "This permission managing sellers");

        // Managing Customers
        Permission manageCustomers = this.createPermission("CRUD_CUSTOMER", "This permission managing customers");

        // Managing Employees
        Permission manageEmployees = this.createPermission("CRUD_EMPLOYEE", "This permission managing employees");

        // Product Management
        Permission manageProducts = this.createPermission("CRUD_PRODUCT", "This permission managing products");

        // Inventory Management
        Permission manageInventory = this.createPermission("CRUD_INVENTORY", "This permission managing inventory");

        // Tracking Management
        Permission trackingUpdate = this.createPermission("TRACKING_UPDATE", "This permission allows user to add tracking step to the shipment");

        Permission trackingCRUD = this.createPermission("TRACKING_CRUD", "This permission allows user to get, convert tracking requests");

        // Creating Roles
        // Creating ADMIN role
        Role adminRole = this.createRole("ADMIN", "Administrator (Access to everything)", Set.of(getUsersPermission, getAllRolesPermission, getAllPermissions, crudRolePermission, crudPermission, crudUserPermission, updateUserPermission, getUserPermission, getRolesPermission, manageCustomers, manageEmployees, manageSellers));

        // Creating USER role
        Role userRole = this.createRole("USER", "User of the application", Set.of(getUserPermission, getRolesPermission, updateUserPermission));

        // Creating SELLER role
        Role sellerRole = this.createRole("SELLER", "Seller of the products", Set.of());

        // Creating CUSTOMER role
        Role customerRole = this.createRole("CUSTOMER", "User of the application", Set.of());

        // Creating EMPLOYEE role
        Role employeeRole = this.createRole("EMPLOYEE", "User of the application", Set.of());

        // Creating SELLER MANAGER role
        Role sellerManagerRole = this.createRole("SELLER_MANAGER", "Manager of the sellers", Set.of(manageSellers));

        // Creating CUSTOMER_MANAGER role
        Role customerManagerRole = this.createRole("CUSTOMER_MANAGER", "Manager of the customers", Set.of(manageCustomers));

        // Creating EMPLOYEE_MANAGER role
        Role employeeManagerRole = this.createRole("EMPLOYEE_MANAGER", "Manager of the employees", Set.of(manageEmployees));

        // Creating PRODUCT_MANAGER role
        Role productManagerRole = this.createRole("PRODUCT_MANAGER", "Manager of the products", Set.of(manageProducts));

        // Creating INVENTORY_MANAGER role
        Role inventoryManagerRole = this.createRole("INVENTORY_MANAGER", "Manager of the inventory", Set.of(manageInventory));

        // Creating LOGISTICS_EXECUTIVE role
        Role logisticsExecutiveRole = this.createRole("LOGISTICS_EXECUTIVE", "Logistics Executive sends the package to the next hop or destination", Set.of(trackingUpdate));

        // Creating TRACKING_MANAGER role
        Role trackingManagerRole = this.createRole("TRACKING_MANAGER", "Manages Tracking, converting requests and check the requests", Set.of(trackingCRUD));

        // Creating User
        // ADMIN user
        User adminUser = new User();
        adminUser.setId(GlobalData.ADMIN_ID);
        adminUser.setEmail(GlobalData.ADMIN_EMAIL);
        adminUser.setPassword(passwordEncoder.encode(userPassword));
        adminUser.setUserType(UserType.EMPLOYEE);
        adminUser.setRoles(Set.of(adminRole, userRole));
        adminUser.setUserState(UserState.APPROVED);
        this.userRepository.save(adminUser);

        // Seller user
        User seller = new User();
        seller.setId(GlobalData.SELLER_ID);
        seller.setEmail(GlobalData.SELLER_EMAIL);
        seller.setPassword(passwordEncoder.encode(userPassword));
        seller.setUserType(UserType.SELLER);
        seller.setRoles(Set.of(userRole, sellerRole));
        seller.setUserState(UserState.APPROVED);
        this.userRepository.save(seller);

        // Seller - 2 user
        User seller2 = new User();
        seller2.setId(GlobalData.SELLER2_ID);
        seller2.setEmail(GlobalData.SELLER2_EMAIL);
        seller2.setPassword(passwordEncoder.encode(userPassword));
        seller2.setUserType(UserType.SELLER);
        seller2.setRoles(Set.of(userRole, sellerRole));
        seller2.setUserState(UserState.APPROVED);
        this.userRepository.save(seller2);

        // Seller - 3 user
        User seller3 = new User();
        seller3.setId(GlobalData.SELLER3_ID);
        seller3.setEmail(GlobalData.SELLER3_EMAIL);
        seller3.setPassword(passwordEncoder.encode(userPassword));
        seller3.setUserType(UserType.SELLER);
        seller3.setRoles(Set.of(userRole, sellerRole));
        seller3.setUserState(UserState.APPROVED);
        this.userRepository.save(seller3);

        // Customer user
        User customer = new User();
        customer.setId(GlobalData.CUSTOMER_ID);
        customer.setEmail(GlobalData.CUSTOMER_EMAIL);
        customer.setPassword(passwordEncoder.encode(userPassword));
        customer.setUserType(UserType.CUSTOMER);
        customer.setRoles(Set.of(userRole, customerRole));
        customer.setUserState(UserState.APPROVED);
        this.userRepository.save(customer);

        // Employee user
        User employee = new User();
        employee.setId(GlobalData.EMPLOYEE_ID);
        employee.setEmail(GlobalData.EMPLOYEE_EMAIL);
        employee.setPassword(passwordEncoder.encode(userPassword));
        employee.setUserType(UserType.EMPLOYEE);
        employee.setRoles(Set.of(userRole, employeeRole));
        employee.setUserState(UserState.APPROVED);
        this.userRepository.save(employee);

        // Seller Manager user
        User sellerManager = new User();
        sellerManager.setId(GlobalData.SELLER_MANAGER_ID);
        sellerManager.setEmail(GlobalData.SELLER_MANAGER_EMAIL);
        sellerManager.setPassword(passwordEncoder.encode(userPassword));
        sellerManager.setUserType(UserType.EMPLOYEE);
        sellerManager.setRoles(Set.of(userRole, employeeRole, sellerManagerRole));
        sellerManager.setUserState(UserState.APPROVED);
        this.userRepository.save(sellerManager);

        // Product Manager user
        User productManager = new User();
        productManager.setId(GlobalData.PRODUCT_MANAGER_ID);
        productManager.setEmail(GlobalData.PRODUCT_MANAGER_EMAIL);
        productManager.setPassword(passwordEncoder.encode(userPassword));
        productManager.setUserType(UserType.EMPLOYEE);
        productManager.setRoles(Set.of(userRole, employeeRole, productManagerRole));
        productManager.setUserState(UserState.APPROVED);
        this.userRepository.save(productManager);

        // Inventory Manager user
        User inventoryManager = new User();
        inventoryManager.setId(GlobalData.INVENTORY_MANAGER_ID);
        inventoryManager.setEmail(GlobalData.INVENTORY_MANAGER_EMAIL);
        inventoryManager.setPassword(passwordEncoder.encode(userPassword));
        inventoryManager.setUserType(UserType.EMPLOYEE);
        inventoryManager.setRoles(Set.of(userRole, employeeRole, inventoryManagerRole));
        inventoryManager.setUserState(UserState.APPROVED);
        this.userRepository.save(inventoryManager);

        // HR Manager user
        User hrManager = new User();
        hrManager.setId(GlobalData.HR_MANAGER_ID);
        hrManager.setEmail(GlobalData.HR_MANAGER_EMAIL);
        hrManager.setPassword(passwordEncoder.encode(userPassword));
        hrManager.setUserType(UserType.EMPLOYEE);
        hrManager.setRoles(Set.of(userRole, employeeRole, employeeManagerRole));
        hrManager.setUserState(UserState.APPROVED);
        this.userRepository.save(hrManager);

        // Customer Manager User
        User customerManager = new User();
        customerManager.setId(GlobalData.CUSTOMER_MANAGER_ID);
        customerManager.setEmail(GlobalData.CUSTOMER_MANAGER_EMAIL);
        customerManager.setPassword(passwordEncoder.encode(userPassword));
        customerManager.setUserType(UserType.EMPLOYEE);
        customerManager.setRoles(Set.of(userRole, employeeRole, customerManagerRole));
        customerManager.setUserState(UserState.APPROVED);
        this.userRepository.save(customerManager);

        // Logistics Executive User
        User logisticsExecutive = new User();
        logisticsExecutive.setId(GlobalData.LOGISTICS_EXECUTIVE_ID);
        logisticsExecutive.setEmail(GlobalData.LOGISTICS_EXECUTIVE_EMAIL);
        logisticsExecutive.setPassword(passwordEncoder.encode(userPassword));
        logisticsExecutive.setUserType(UserType.EMPLOYEE);
        logisticsExecutive.setRoles(Set.of(userRole, employeeRole, logisticsExecutiveRole));
        logisticsExecutive.setUserState(UserState.APPROVED);
        this.userRepository.save(logisticsExecutive);

        // Tracking Manager User
        User trackingManager = new User();
        trackingManager.setId(GlobalData.TRACKING_MANAGER_ID);
        trackingManager.setEmail(GlobalData.TRACKING_MANAGER_EMAIL);
        trackingManager.setPassword(passwordEncoder.encode(userPassword));
        trackingManager.setUserType(UserType.EMPLOYEE);
        trackingManager.setRoles(Set.of(userRole, employeeRole, trackingManagerRole));
        trackingManager.setUserState(UserState.APPROVED);
        this.userRepository.save(trackingManager);

        alreadySetup = true;
    }

    private Permission createPermission(String permissionName, String description) {
        Permission permission = new Permission();
        permission.setPermissionName(permissionName);
        permission.setDescription(description);
        permission.setId(UUID.randomUUID().toString());
        return this.permissionRepository.save(permission);
    }

    private Role createRole(String roleName, String description, Set<Permission> permissions) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setDescription(description);
        role.setPermissions(permissions);
        role.setId(UUID.randomUUID().toString());
        return this.roleRepository.save(role);
    }
}
