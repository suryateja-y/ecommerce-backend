package com.academy.projects.ecommerce.usermanagementservice.kafka.producer.configuration;

import com.academy.projects.ecommerce.usermanagementservice.kafka.dtos.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfiguration {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, StatusUpdateRequestDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, StatusUpdateRequestDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, SellerDto> sellerProducerFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, SellerDto> sellerKafkaTemplate() {
        return new KafkaTemplate<>(sellerProducerFactory());
    }

    @Bean
    public ProducerFactory<String, CustomerDto> customerProducerFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, CustomerDto> customerKafkaTemplate() {
        return new KafkaTemplate<>(customerProducerFactory());
    }

    @Bean
    public ProducerFactory<String, EmployeeDto> employeeProducerFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, EmployeeDto> employeeKafkaTemplate() {
        return new KafkaTemplate<>(employeeProducerFactory());
    }

    @Bean
    public ProducerFactory<String, UserUpdateDto> userUpdateProducerFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, UserUpdateDto> userUpdateKafkaTemplate() {
        return new KafkaTemplate<>(userUpdateProducerFactory());
    }

    private Map<String, Object> configProperties() {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProperties;
    }

}
