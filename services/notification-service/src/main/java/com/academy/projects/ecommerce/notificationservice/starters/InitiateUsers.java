package com.academy.projects.ecommerce.notificationservice.starters;

import com.academy.projects.ecommerce.notificationservice.models.User;
import com.academy.projects.ecommerce.notificationservice.models.UserState;
import com.academy.projects.ecommerce.notificationservice.models.UserType;
import com.academy.projects.ecommerce.notificationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitiateUsers {
    private final UserRepository userRepository;

    @Autowired
    public InitiateUsers(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void initialize() {
        // Seller
        User seller = new User();
        seller.setFullName("Academy Seller");
        seller.setEmail(GlobalData.SELLER_EMAIL);
        seller.setUserId(GlobalData.SELLER_ID);
        seller.setUserState(UserState.APPROVED);
        seller.setUserType(UserType.SELLER);
        userRepository.save(seller);

        // Seller Manager
        User sellerManager = new User();
        sellerManager.setFullName("Academy Seller Manager");
        sellerManager.setEmail(GlobalData.SELLER_MANAGER_EMAIL);
        sellerManager.setUserId(GlobalData.SELLER_MANAGER_ID);
        sellerManager.setUserState(UserState.APPROVED);
        sellerManager.setUserType(UserType.EMPLOYEE);
        userRepository.save(sellerManager);

        // Product Manager
        User productManager = new User();
        productManager.setFullName("Academy Product Manager");
        productManager.setEmail(GlobalData.PRODUCT_MANAGER_EMAIL);
        productManager.setUserId(GlobalData.PRODUCT_MANAGER_ID);
        productManager.setUserState(UserState.APPROVED);
        productManager.setUserType(UserType.EMPLOYEE);
        userRepository.save(productManager);

        // Admin
        User admin = new User();
        admin.setFullName("Academy Admin");
        admin.setEmail(GlobalData.ADMIN_EMAIL);
        admin.setUserId(GlobalData.ADMIN_ID);
        admin.setUserState(UserState.APPROVED);
        admin.setUserType(UserType.EMPLOYEE);
        userRepository.save(admin);

        // Customer
        User customer = new User();
        customer.setFullName("Academy Customer");
        customer.setEmail(GlobalData.CUSTOMER_EMAIL);
        customer.setUserId(GlobalData.CUSTOMER_ID);
        customer.setUserState(UserState.APPROVED);
        customer.setUserType(UserType.CUSTOMER);
        userRepository.save(customer);
    }

}
