package com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user;

import com.academy.projects.ecommerce.usermanagementservice.dtos.ActionType;
import com.academy.projects.ecommerce.usermanagementservice.kafka.dtos.CustomerDto;
import com.academy.projects.ecommerce.usermanagementservice.models.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerUpdateManager implements ICustomerUpdateManager {
    @Value("${application.kafka.topics.customer-update-topic}")
    private String customerUpdateTopic;

    private final KafkaTemplate<String, CustomerDto> customerUpdateKafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(CustomerUpdateManager.class);

    @Autowired
    public CustomerUpdateManager(KafkaTemplate<String, CustomerDto> customerUpdateKafkaTemplate) {
        this.customerUpdateKafkaTemplate = customerUpdateKafkaTemplate;
    }

    @Override
    public void sendRegistration(Customer customer) {
        customerUpdateKafkaTemplate.send(customerUpdateTopic, from(customer, ActionType.CREATE));
        logger.info("Sent Customer Registration to the Observers. Customer: '{}'!!!", customer.getId());
    }

    @Override
    public void sendUpdate(Customer customer) {
        customerUpdateKafkaTemplate.send(customerUpdateTopic, from(customer, ActionType.UPDATE));
        logger.info("Sent Customer Update to the Observers. Customer: '{}'!!!", customer.getId());
    }

    private CustomerDto from(Customer customer, ActionType action) {
        return CustomerDto.builder()
                .action(action)
                .customer(customer)
                .build();
    }
}
