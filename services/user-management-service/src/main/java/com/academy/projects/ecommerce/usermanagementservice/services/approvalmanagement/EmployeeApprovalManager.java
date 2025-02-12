package com.academy.projects.ecommerce.usermanagementservice.services.approvalmanagement;

import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.ApprovalResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.ResponseStatus;
import com.academy.projects.ecommerce.usermanagementservice.clients.services.ApprovalManagementServiceClient;
import com.academy.projects.ecommerce.usermanagementservice.dtos.ActionType;
import com.academy.projects.ecommerce.usermanagementservice.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.usermanagementservice.dtos.ApprovalStatus;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.EmployeeApprovalRegistrationException;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.UserNotFoundException;
import com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user.IEmployeeUpdateManager;
import com.academy.projects.ecommerce.usermanagementservice.models.Employee;
import com.academy.projects.ecommerce.usermanagementservice.models.User;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import com.academy.projects.ecommerce.usermanagementservice.repositories.EmployeeRepository;
import com.academy.projects.ecommerce.usermanagementservice.repositories.UserRepository;
import com.academy.projects.ecommerce.usermanagementservice.starters.GlobalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeApprovalManager implements IUserApprovalManager {

    @Value("${application.kafka.topics.user-approval-topic}")
    private String employeeApprovalTopic;

    private final ApprovalManagementServiceClient approvalManagementServiceClient;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final IEmployeeUpdateManager employeeUpdateManager;

    @Autowired
    public EmployeeApprovalManager(ApprovalManagementServiceClient approvalManagementServiceClient, EmployeeRepository employeeRepository, UserRepository userRepository, IEmployeeUpdateManager employeeUpdateManager) {
        this.approvalManagementServiceClient = approvalManagementServiceClient;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.employeeUpdateManager = employeeUpdateManager;
    }

    public String register(Employee employee) {
        try {
            ApprovalRequest approvalRequest = new ApprovalRequest();
            approvalRequest.setRequester(employee.getId());
            approvalRequest.setTopic(employeeApprovalTopic);
            approvalRequest.setTitle("Employee Approval");
            approvalRequest.setData(employee);
            approvalRequest.setApprovers(List.of(GlobalData.EMPLOYEE_MANAGER_ID, GlobalData.HR_MANAGER_ID, GlobalData.ADMIN_ID));
            approvalRequest.setActionType(ActionType.CREATE);
            ApprovalResponseDto responseDto = approvalManagementServiceClient.registerUserForApproval(approvalRequest);
            if(responseDto.getResponseStatus() == ResponseStatus.SUCCESS)
                return responseDto.getApprovalId();
            else throw new EmployeeApprovalRegistrationException(employee.getId());
        } catch(Exception e) {
            throw new EmployeeApprovalRegistrationException(employee.getId());
        }
    }

    @Override
    public void updateStatus(ApprovalRequest approvalRequest) {
        Employee employee = employeeRepository.findById(approvalRequest.getRequester()).orElseThrow(() -> new UserNotFoundException(approvalRequest.getRequester()));
        User user = employee.getUser();
        user.setUserState(from(approvalRequest.getStatus()));
        user = userRepository.save(user);
        employee.setUser(user);
        employee = employeeRepository.save(employee);

        // Sending status update to the consumers
        employeeUpdateManager.sendStatusUpdate(employee);
    }

    private UserState from(ApprovalStatus approvalStatus) {
        if(approvalStatus == ApprovalStatus.APPROVED) return UserState.APPROVED;
        if(approvalStatus == ApprovalStatus.REJECTED) return UserState.REJECTED;
        return UserState.PENDING_FOR_APPROVAL;
    }
}
