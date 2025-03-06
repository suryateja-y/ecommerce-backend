package com.academy.projects.ecommerce.ordermanagementservice.kafka.consumers.configurations;

import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.PaymentDto;
import com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos.TrackingDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class ConsumerConfiguration {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, PaymentDto> paymentConsumerFactory() {
        JsonDeserializer<PaymentDto> jsonDeserializer = new JsonDeserializer<>(PaymentDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/ordermanagementservice/kafka/dtos/PaymentDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentDto> kafkaListenerContainerFactoryForPayment() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ApprovalRequest> approvalConsumerFactory() {
        JsonDeserializer<ApprovalRequest> jsonDeserializer = new JsonDeserializer<>(ApprovalRequest.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/ordermanagementservice/kafka/dtos/ApprovalRequest");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApprovalRequest> kafkaListenerContainerFactoryForInventory() {
        ConcurrentKafkaListenerContainerFactory<String, ApprovalRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(approvalConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, TrackingDto> trackingConsumerFactory() {
        JsonDeserializer<TrackingDto> jsonDeserializer = new JsonDeserializer<>(TrackingDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/ordermanagementservice/kafka/dtos/TrackingDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TrackingDto> kafkaListenerContainerFactoryForTracking() {
        ConcurrentKafkaListenerContainerFactory<String, TrackingDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(trackingConsumerFactory());
        return factory;
    }

    private Map<String, Object> configurationProperties(JsonDeserializer<?> jsonDeserializer) {
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.setRemoveTypeHeaders(false);
        Map<String, Object> configurationProperties = new HashMap<>();
        configurationProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configurationProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurationProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, jsonDeserializer);
        configurationProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return configurationProperties;
    }
}
