package com.academy.projects.ecommerce.usermanagementservice.services.usermanagement;

import com.academy.projects.ecommerce.usermanagementservice.dtos.EmployeeRegistrationDto;
import com.academy.projects.ecommerce.usermanagementservice.dtos.UpdateEmployerDetailsRequestDto;
import com.academy.projects.ecommerce.usermanagementservice.models.Address;
import com.academy.projects.ecommerce.usermanagementservice.models.Employee;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;

import java.util.List;

public interface IEmployeeManagementService {
    Employee register(EmployeeRegistrationDto employeeDto);
    Employee update(Employee employee);
    Employee getEmployee(String id);
    List<Employee> getEmployees(int page, int pageSize, UserState userState);
    Employee updateState(String userId, UserState userState, String updaterId);
    Employee updateEmployerDetails(String userId, UpdateEmployerDetailsRequestDto requestDto);
    Employee addAddress(String userId, Address address);
    Employee updateAddress(String userId, Address address);
    Address getAddress(String userId);
}
