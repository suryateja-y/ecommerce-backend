package com.academy.projects.ecommerce.productonboardingservice.kafka.producer.configurations;

import com.academy.projects.ecommerce.productonboardingservice.kafka.dtos.CategoryDto;
import com.academy.projects.ecommerce.productonboardingservice.kafka.dtos.InventoryDto;
import com.academy.projects.ecommerce.productonboardingservice.kafka.dtos.ProductDto;
import com.academy.projects.ecommerce.productonboardingservice.kafka.dtos.VariantDto;
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

    @Value("${application.kafka.host}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, ProductDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, ProductDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, VariantDto> variantProducerFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, VariantDto> variantKafkaTemplate() {
        return new KafkaTemplate<>(variantProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, CategoryDto> categoryKafkaTemplate() {
        return new KafkaTemplate<>(categoryProducerFactory());
    }

    @Bean
    public ProducerFactory<String, CategoryDto>categoryProducerFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public ProducerFactory<String, InventoryDto> inventoryUnitProducerFactory() {
        return new DefaultKafkaProducerFactory<>(configProperties());
    }

    @Bean
    public KafkaTemplate<String, InventoryDto> inventoryKafkaTemplate() {
        return new KafkaTemplate<>(inventoryUnitProducerFactory());
    }

    private Map<String, Object> configProperties() {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProperties;
    }
}
