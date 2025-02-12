package com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user;

import com.academy.projects.ecommerce.usermanagementservice.dtos.ActionType;
import com.academy.projects.ecommerce.usermanagementservice.kafka.dtos.EmployeeDto;
import com.academy.projects.ecommerce.usermanagementservice.models.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmployeeUpdateManager implements IEmployeeUpdateManager {
    private final KafkaTemplate<String, EmployeeDto> employeeKafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(EmployeeUpdateManager.class);

    @Value("${application.kafka.topics.employee-update-topic}")
    private String employeeUpdateTopic;

    public EmployeeUpdateManager(KafkaTemplate<String, EmployeeDto> employeeKafkaTemplate) {
        this.employeeKafkaTemplate = employeeKafkaTemplate;
    }

    @Override
    public void sendRegistration(Employee employee) {
        employeeKafkaTemplate.send(employeeUpdateTopic, from(employee, ActionType.CREATE));
        logger.info("Sent Employee Registration to the Observers. Employee: '{}'!!!", employee.getId());
    }

    @Override
    public void sendUpdate(Employee employee) {
        employeeKafkaTemplate.send(employeeUpdateTopic, from(employee, ActionType.UPDATE));
        logger.info("Sent Employee Update to the Observers. Employee: '{}'!!!", employee.getId());
    }

    @Override
    public void sendEmployerDetailsUpdate(Employee employee) {
        employeeKafkaTemplate.send(employeeUpdateTopic, from(employee, ActionType.DETAILS_UPDATE));
        logger.info("Sent Employee Details update to the Observers. Employee: '{}'!!!", employee.getId());
    }

    @Override
    public void sendStatusUpdate(Employee employee) {
        employeeKafkaTemplate.send(employeeUpdateTopic, from(employee, ActionType.STATUS_UPDATE));
        logger.info("Sent Employee Status update to the Observers. Employee: '{}'!!!", employee.getId());
    }

    private EmployeeDto from(Employee employee, ActionType action) {
        return EmployeeDto.builder()
                .action(action)
                .employee(employee)
                .build();
    }
}
