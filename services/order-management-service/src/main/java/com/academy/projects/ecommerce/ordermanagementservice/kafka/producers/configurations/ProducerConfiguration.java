package com.academy.projects.ecommerce.ordermanagementservice.kafka.producers.configurations;

import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.InventoryUnitDto;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.OrderDto;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.PreOrderDto;
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
    public ProducerFactory<String, OrderDto> orderFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, OrderDto> kafkaTemplate() {
        return new KafkaTemplate<>(orderFactory());
    }

    @Bean
    public ProducerFactory<String, PreOrderDto> preOrderFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, PreOrderDto> preOrderKafkaTemplate() {
        return new KafkaTemplate<>(preOrderFactory());
    }

    @Bean
    public ProducerFactory<String, InventoryUnitDto> inventoryFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, InventoryUnitDto> inventoryKafkaTemplate() {
        return new KafkaTemplate<>(inventoryFactory());
    }

    private Map<String, Object> configProperties() {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProperties;
    }
}
