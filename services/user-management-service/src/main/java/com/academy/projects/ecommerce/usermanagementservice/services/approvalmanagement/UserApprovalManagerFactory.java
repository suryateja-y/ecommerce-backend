package com.academy.projects.ecommerce.usermanagementservice.services.approvalmanagement;

import com.academy.projects.ecommerce.usermanagementservice.models.UserType;
import org.springframework.stereotype.Service;

@Service
public class UserApprovalManagerFactory {
    private final SellerApprovalManager sellerApprovalManager;
    private final EmployeeApprovalManager employeeApprovalManager;

    public UserApprovalManagerFactory(final SellerApprovalManager sellerApprovalManager, final EmployeeApprovalManager employeeApprovalManager) {
        this.sellerApprovalManager = sellerApprovalManager;
        this.employeeApprovalManager = employeeApprovalManager;
    }

    public IUserApprovalManager getApprovalManager(UserType userType) {
        return switch (userType) {
            case SELLER -> sellerApprovalManager;
            case EMPLOYEE -> employeeApprovalManager;
            default -> throw new IllegalArgumentException("Unsupported user type: " + userType);
        };
    }
}
