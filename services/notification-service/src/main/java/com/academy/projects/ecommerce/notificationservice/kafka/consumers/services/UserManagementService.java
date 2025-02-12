package com.academy.projects.ecommerce.notificationservice.kafka.consumers.services;

import com.academy.projects.ecommerce.notificationservice.exceptions.UnsupportedUserException;
import com.academy.projects.ecommerce.notificationservice.kafka.dtos.*;
import com.academy.projects.ecommerce.notificationservice.models.RecipientCommunicationDetails;
import com.academy.projects.ecommerce.notificationservice.models.User;
import com.academy.projects.ecommerce.notificationservice.models.UserState;
import com.academy.projects.ecommerce.notificationservice.models.UserType;
import com.academy.projects.ecommerce.notificationservice.services.notifications.INotificationManagementService;
import com.academy.projects.ecommerce.notificationservice.services.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class UserManagementService {

    private final INotificationManagementService notificationManagementService;
    private final IUserService userService;

    @Autowired
    public UserManagementService(INotificationManagementService notificationManagementService, IUserService userService) {
        this.notificationManagementService = notificationManagementService;
        this.userService = userService;
    }

    private final Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    @KafkaListener(topics = "${application.kafka.topics.customer-update-topic}", groupId = "${application.kafka.consumer.customer-update-group}", containerFactory = "kafkaListenerContainerFactoryForCustomerUpdate")
    public void consumeCustomer(CustomerUpdateDto customerUpdateDto) {
        try {
            String registryKey = getRegistryKey(customerUpdateDto.getAction(), UserType.CUSTOMER, customerUpdateDto.getCustomer().getUser().getUserState());
            RecipientCommunicationDetails communicationDetails = getCommunicationDetails(customerUpdateDto.getCustomer().getUser());
            notificationManagementService.send(registryKey, prepareData(customerUpdateDto.getCustomer().getUser()), communicationDetails);
            userService.save(customerUpdateDto.getCustomer().getUser());
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.seller-update-topic}", groupId = "${application.kafka.consumer.seller-update-group}", containerFactory = "kafkaListenerContainerFactoryForSellerUpdate")
    public void consumeSeller(SellerUpdateDto sellerUpdateDto) {
        try {
            String registryKey = getRegistryKey(sellerUpdateDto.getAction(), UserType.SELLER, sellerUpdateDto.getSeller().getUser().getUserState());
            RecipientCommunicationDetails communicationDetails = getCommunicationDetails(sellerUpdateDto.getSeller().getUser());
            notificationManagementService.send(registryKey, prepareData(sellerUpdateDto.getSeller()), communicationDetails);
            userService.save(sellerUpdateDto.getSeller().getUser());
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.employee-update-topic}", groupId = "${application.kafka.consumer.employee-update-group}", containerFactory = "kafkaListenerContainerFactoryForEmployeeUpdate")
    public void consumeEmployee(EmployeeUpdateDto employeeUpdateDto) {
        try {
            String registryKey = getRegistryKey(employeeUpdateDto.getAction(), UserType.EMPLOYEE, employeeUpdateDto.getEmployee().getUser().getUserState());
            RecipientCommunicationDetails communicationDetails = getCommunicationDetails(employeeUpdateDto.getEmployee().getUser());
            notificationManagementService.send(registryKey, prepareData(employeeUpdateDto.getEmployee()), communicationDetails);
            userService.save(employeeUpdateDto.getEmployee().getUser());
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${application.kafka.topics.user-update-topic}", groupId = "${application.kafka.consumer.user-update-group}", containerFactory = "kafkaListenerContainerFactoryForUserUpdate")
    public void consumerUser(UserUpdateDto userUpdateDto) {
        try {
            String registryKey = getRegistryKey(userUpdateDto.getAction(), userUpdateDto.getUser().getUserType(), userUpdateDto.getUser().getUserState());
            RecipientCommunicationDetails communicationDetails = getCommunicationDetails(userUpdateDto.getUser());
            notificationManagementService.send(registryKey, prepareData(userUpdateDto.getUser()), communicationDetails);
            userService.save(userUpdateDto.getUser());
        } catch(Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getRegistryKey(Action action, UserType userType, UserState userState) {
        switch (userType) {
            case CUSTOMER -> {
                return switch (action) {
                    case CREATE -> "customer-registration";
                    case UPDATE -> "user-update";
                    case DELETE, DETAILS_UPDATE, STATUS_UPDATE -> "";
                };
            }
            case EMPLOYEE -> {
                return switch (action) {
                    case CREATE -> "employee-registration";
                    case UPDATE -> "user-update";
                    case DETAILS_UPDATE -> "employment-details";
                    case DELETE -> "";
                    case STATUS_UPDATE -> ((userState != null) && userState.equals(UserState.APPROVED)) ? "employee-approved" : "user-state";
                };
            }
            case SELLER -> {
                return switch (action) {
                    case CREATE -> "seller-registration";
                    case UPDATE -> "user-update";
                    case DETAILS_UPDATE -> "seller-address";
                    case DELETE -> "";
                    case STATUS_UPDATE -> ((userState != null) && userState.equals(UserState.APPROVED)) ? "seller-approved" : "user-state";
                };
            }
            default -> throw new UnsupportedUserException("Unexpected value: " + userType);
        }

    }

    private RecipientCommunicationDetails getCommunicationDetails(User user) {
        return RecipientCommunicationDetails.builder()
                .email(user.getEmail())
                .build();
    }

    private Map<String, Object> prepareData(User user) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userName", user.getFullName());
        data.put("userId", user.getUserId());
        data.put("email", user.getEmail());
        data.put("userType", user.getUserType());
        data.put("status", user.getUserState());
        return data;
    }

    @SuppressWarnings("DuplicatedCode")
    private Map<String, Object> prepareData(Seller seller) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userName", seller.getUser().getFullName());
        Map<String, Object> sellerData = new LinkedHashMap<>();
        sellerData.put("User ID", seller.getUser().getUserId());
        sellerData.put("User Name", seller.getUser().getFullName());
        sellerData.put("Email", seller.getUser().getEmail());
        sellerData.put("Brand Name", seller.getBrandName());
        sellerData.put("Company Name", seller.getCompanyName());
        data.put("sellerData", sellerData);
        data.put("approvalId", seller.getApprovalId());
        data.put("userType", UserType.SELLER);
        data.put("address", seller.getAddress());
        return data;
    }

    @SuppressWarnings("DuplicatedCode")
    private Map<String, Object> prepareData(Employee employee) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userName", employee.getUser().getFullName());
        Map<String, Object> employeeData = new LinkedHashMap<>();
        employeeData.put("User ID", employee.getUser().getUserId());
        employeeData.put("User Name", employee.getUser().getFullName());
        employeeData.put("Email", employee.getUser().getEmail());
        data.put("employeeData", employeeData);
        data.put("approvalId", employee.getApprovalId());
        data.put("userType", UserType.EMPLOYEE);
        data.put("employeeId", employee.getEmployeeId());
        data.put("designation", employee.getDesignation());
        return data;
    }
}
