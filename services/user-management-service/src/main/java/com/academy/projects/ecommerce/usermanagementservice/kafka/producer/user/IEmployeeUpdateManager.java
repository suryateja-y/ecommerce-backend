package com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user;

import com.academy.projects.ecommerce.usermanagementservice.models.Employee;

public interface IEmployeeUpdateManager {
    void sendRegistration(Employee employee);
    void sendUpdate(Employee employee);
    void sendEmployerDetailsUpdate(Employee employee);
    void sendStatusUpdate(Employee employee);
}
