package com.academy.projects.ecommerce.usermanagementservice.controllers;

import com.academy.projects.ecommerce.usermanagementservice.dtos.EmployeeRegistrationDto;
import com.academy.projects.ecommerce.usermanagementservice.dtos.UpdateEmployerDetailsRequestDto;
import com.academy.projects.ecommerce.usermanagementservice.dtos.UpdateStatusRequestDto;
import com.academy.projects.ecommerce.usermanagementservice.dtos.UserRegistrationResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.models.Address;
import com.academy.projects.ecommerce.usermanagementservice.models.Employee;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import com.academy.projects.ecommerce.usermanagementservice.services.usermanagement.IEmployeeManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/users/employees")
public class EmployeeController {

    private final IEmployeeManagementService employeeManagementService;

    @Autowired
    public EmployeeController(final IEmployeeManagementService employeeManagementService) {
        this.employeeManagementService = employeeManagementService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto> register(@RequestBody EmployeeRegistrationDto employeeDto) {
        UserRegistrationResponseDto responseDto = new UserRegistrationResponseDto();
        Employee employee = employeeManagementService.register(employeeDto);
        responseDto.setMessage("Employee registered successfully!!!");
        responseDto.setUserId(employee.getId());
        responseDto.setCreatedOrModifiedAt(employee.getCreatedAt());
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE') and @accessChecker.isOwner(#employee.id)")
    @ResponseStatus(HttpStatus.OK)
    public Employee updateEmployee(@RequestBody Employee employee) {
        return employeeManagementService.update(employee);
    }

    @PreAuthorize("hasAnyAuthority('CRUD_USER', 'CRUD_EMPLOYEE')")
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getEmployee(@PathVariable String userId) {
        return employeeManagementService.getEmployee(userId);
    }

    @PreAuthorize("hasRole('ROLE_USERR')")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Employee getEmployee(Authentication authentication) {
        return employeeManagementService.getEmployee(authentication.getName());
    }

    @PreAuthorize("hasAnyAuthority('CRUD_USER', 'CRUD_EMPLOYEE')")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployees(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize, @RequestParam(required = false) UserState userState) {
        return employeeManagementService.getEmployees(page, pageSize, userState);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('CRUD_USER', 'CRUD_EMPLOYEE')")
    public Employee updateState(@PathVariable String userId, @RequestBody UpdateStatusRequestDto requestDto, Authentication authentication) {
        return employeeManagementService.updateState(userId, requestDto.getUserState(), authentication.getName());
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('CRUD_USER', 'CRUD_EMPLOYEE') or hasAnyRole('ROLE_ADMIN', 'ROLE_HR_MANAGER')")
    public Employee updateEmployerDetails(@PathVariable String userId, @RequestBody UpdateEmployerDetailsRequestDto requestDto) {
        return employeeManagementService.updateEmployerDetails(userId, requestDto);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @PostMapping("/addresses")
    public ResponseEntity<Employee> addAddress(Authentication authentication, @RequestBody Address address) {
        Employee employee = employeeManagementService.addAddress(authentication.getName(), address);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @PatchMapping("/addresses")
    public ResponseEntity<Employee> updateAddress(Authentication authentication, @RequestBody Address address) {
        Employee employee = employeeManagementService.updateAddress(authentication.getName(), address);
        return new ResponseEntity<>(employee, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @GetMapping("/addresses")
    public ResponseEntity<Address> getAddress(Authentication authentication) {
        Address address = employeeManagementService.getAddress(authentication.getName());
        return new ResponseEntity<>(address, HttpStatus.OK);
    }
}
