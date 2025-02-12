package com.academy.projects.ecommerce.usermanagementservice.services.usermanagement;

import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.SignUpRequestDto;
import com.academy.projects.ecommerce.usermanagementservice.configurations.Patcher;
import com.academy.projects.ecommerce.usermanagementservice.dtos.EmployeeRegistrationDto;
import com.academy.projects.ecommerce.usermanagementservice.dtos.UpdateEmployerDetailsRequestDto;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.IdNotProvidedException;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.InvalidateStateRequest;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.UserAlreadyExistsException;
import com.academy.projects.ecommerce.usermanagementservice.exceptions.UserNotFoundException;
import com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user.IEmployeeUpdateManager;
import com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user.IUserUpdateManager;
import com.academy.projects.ecommerce.usermanagementservice.models.*;
import com.academy.projects.ecommerce.usermanagementservice.repositories.EmployeeRepository;
import com.academy.projects.ecommerce.usermanagementservice.services.approvalmanagement.EmployeeApprovalManager;
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
public class EmployeeManagementService implements IEmployeeManagementService {
    private final EmployeeRepository employeeRepository;
    private final IUserManagementService userManagementService;
    private final Patcher patcher;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeApprovalManager employeeApprovalManager;
    private final IEmployeeUpdateManager employeeUpdateManager;
    private final IUserUpdateManager userUpdateManager;

    private final Logger logger = LoggerFactory.getLogger(EmployeeManagementService.class);

    @Autowired
    public EmployeeManagementService(EmployeeRepository employeeRepository, IUserManagementService userManagementService, Patcher patcher, PasswordEncoder passwordEncoder, EmployeeApprovalManager employeeApprovalManager, IEmployeeUpdateManager employeeUpdateManager, IUserUpdateManager userUpdateManager) {
        this.employeeRepository = employeeRepository;
        this.userManagementService = userManagementService;
        this.patcher = patcher;
        this.passwordEncoder = passwordEncoder;
        this.employeeApprovalManager = employeeApprovalManager;
        this.employeeUpdateManager = employeeUpdateManager;
        this.userUpdateManager = userUpdateManager;
    }

    @Override
    public Employee register(EmployeeRegistrationDto employeeDto) {
        if(userManagementService.userExists(employeeDto.getEmail(), UserType.EMPLOYEE)) throw new UserAlreadyExistsException(employeeDto.getEmail(), UserType.EMPLOYEE);

        // Registering Employee into the Authentication Service
        String userId = userManagementService.registerInAuthenticationService(SignUpRequestDto.builder()
                .email(employeeDto.getEmail())
                .password(passwordEncoder.encode(employeeDto.getPassword()))
                .userType(UserType.EMPLOYEE)
                .build());

        User user = new User();
        user.setEmail(employeeDto.getEmail());
        user.setFullName(employeeDto.getFullName());
        user.setUserType(UserType.EMPLOYEE);
        user.setUserState(UserState.PENDING_FOR_APPROVAL);
        user.setPhoneNumber(employeeDto.getPhoneNumber());
        user.setId(userId);
        User newUser = userManagementService.save(user);

        Employee employee = new Employee();
        employee.setBloodGroup(employeeDto.getBloodGroup());
        employee.setAge(employeeDto.getAge());
        employee.setGender(employeeDto.getGender());
        employee.setUser(newUser);
        employee.setId(userId);

        // Sending Employee for approval
        String approvalRequestId = this.employeeApprovalManager.register(employee);

        // Send notification with approval request details
        logger.info("Employee Approval Request Id: {}", approvalRequestId);

        employee.setApprovalId(approvalRequestId);
        employee = employeeRepository.save(employee);

        // Send a notification after the Employer sent for the approval
        employeeUpdateManager.sendRegistration(employee);
        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        if(employee.getId() == null) throw new IdNotProvidedException();
        Employee savedEmployee = employeeRepository.findById(employee.getId()).orElseThrow(() -> new UserNotFoundException(employee.getId()));
        User savedUser = savedEmployee.getUser();
        patcher.entity(savedUser, savedEmployee.getUser(), User.class);
        patcher.entity(savedEmployee, employee, Employee.class);
        savedUser.setId(savedEmployee.getId());
        savedEmployee.setUser(savedUser);

        // Sending Employee for the approval
        String approvalRequestId = this.employeeApprovalManager.register(savedEmployee);
        logger.info("Employee Update Approval Request Id: {}", approvalRequestId);

        // Save Update to Database
        savedUser = userManagementService.save(savedUser);
        savedEmployee.setUser(savedUser);
        savedEmployee = employeeRepository.save(savedEmployee);

        // Send a notification after the Update sent for the approval
        employeeUpdateManager.sendUpdate(savedEmployee);
        return savedEmployee;
    }

    @Override
    public Employee getEmployee(String id) {
        return employeeRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<Employee> getEmployees(int page, int pageSize, UserState userState) {
        if(userState != null) return this.getEmployeesByUserState(page, pageSize, userState);
        Pageable pageable = PageRequest.of(page, pageSize);
        return employeeRepository.findAll(pageable).getContent();
    }

    private List<Employee> getEmployeesByUserState(int page, int pageSize, UserState userState) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Employee> employeePage = employeeRepository.findAllByUser_UserState(userState, pageable);
        return employeePage.getContent();
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public Employee updateState(String userId, UserState userState, String updaterId) {
        if(!(userState.equals(UserState.IN_ACTIVE) || userState.equals(UserState.REJECTED))) throw new InvalidateStateRequest(userState);
        if(!employeeRepository.existsById(userId)) throw new UserNotFoundException(userId);
        userManagementService.updateState(userId, userState);
        User savedUser = userManagementService.get(userId);
        Employee savedEmployee = employeeRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        savedEmployee.setUser(savedUser);
        savedEmployee = employeeRepository.save(savedEmployee);

        // Sending update to Kafka
        userUpdateManager.sendUserUpdate(savedEmployee.getUser(), savedEmployee.getApprovalId(), updaterId);
        return savedEmployee;
    }

    @Override
    public Employee updateEmployerDetails(String userId, UpdateEmployerDetailsRequestDto requestDto) {
        Employee savedEmployee = employeeRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        savedEmployee.setEmployeeId(requestDto.getEmployeeId());
        savedEmployee.setDesignation(requestDto.getDesignation());
        savedEmployee = employeeRepository.save(savedEmployee);
        // Send a notification to the Employee
        employeeUpdateManager.sendEmployerDetailsUpdate(savedEmployee);
        return savedEmployee;
    }

    @Override
    public Employee addAddress(String userId, Address address) {
        Employee savedEmployee = employeeRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        savedEmployee.setAddress(address);
        savedEmployee = employeeRepository.save(savedEmployee);
        logger.info("Address: '{}' has been added into the Employee: '{}'!!!", address.getId(), savedEmployee.getUser().getEmail());
        return savedEmployee;
    }

    @Override
    public Employee updateAddress(String userId, Address address) {
        if(address.getId() == null) throw new IdNotProvidedException();
        Employee savedEmployee = employeeRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        savedEmployee.setAddress(address);
        savedEmployee = employeeRepository.save(savedEmployee);
        logger.info("Address: '{}' has been updated into the Employee: '{}'!!!", address.getId(), savedEmployee.getUser().getEmail());
        return savedEmployee;
    }

    @Override
    public Address getAddress(String userId) {
        Employee employee = employeeRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return employee.getAddress();
    }
}
