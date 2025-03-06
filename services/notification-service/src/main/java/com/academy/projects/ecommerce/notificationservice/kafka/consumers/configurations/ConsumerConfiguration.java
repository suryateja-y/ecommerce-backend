package com.academy.projects.ecommerce.notificationservice.kafka.consumers.configurations;

import com.academy.projects.ecommerce.notificationservice.kafka.dtos.*;
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
    public ConsumerFactory<String, OrderDto> orderConsumerFactory() {
        JsonDeserializer<OrderDto> jsonDeserializer = new JsonDeserializer<>(OrderDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/OrderDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDto> kafkaListenerContainerFactoryForOrder() {
        ConcurrentKafkaListenerContainerFactory<String, OrderDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CustomerUpdateDto> customerUpdateConsumerFactory() {
        JsonDeserializer<CustomerUpdateDto> jsonDeserializer = new JsonDeserializer<>(CustomerUpdateDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/CustomerUpdateDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomerUpdateDto> kafkaListenerContainerFactoryForCustomerUpdate() {
        ConcurrentKafkaListenerContainerFactory<String, CustomerUpdateDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(customerUpdateConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, EmployeeUpdateDto> employeeUpdateConsumerFactory() {
        JsonDeserializer<EmployeeUpdateDto> jsonDeserializer = new JsonDeserializer<>(EmployeeUpdateDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/EmployeeUpdateDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmployeeUpdateDto> kafkaListenerContainerFactoryForEmployeeUpdate() {
        ConcurrentKafkaListenerContainerFactory<String, EmployeeUpdateDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(employeeUpdateConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, SellerUpdateDto> sellerUpdateConsumerFactory() {
        JsonDeserializer<SellerUpdateDto> jsonDeserializer = new JsonDeserializer<>(SellerUpdateDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/SellerUpdateDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SellerUpdateDto> kafkaListenerContainerFactoryForSellerUpdate() {
        ConcurrentKafkaListenerContainerFactory<String, SellerUpdateDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(sellerUpdateConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, UserUpdateDto> userUpdateConsumerFactory() {
        JsonDeserializer<UserUpdateDto> jsonDeserializer = new JsonDeserializer<>(UserUpdateDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/UserUpdateDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserUpdateDto> kafkaListenerContainerFactoryForUserUpdate() {
        ConcurrentKafkaListenerContainerFactory<String, UserUpdateDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userUpdateConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ApprovalNotificationDto> approvalUpdateConsumerFactory() {
        JsonDeserializer<ApprovalNotificationDto> jsonDeserializer = new JsonDeserializer<>(ApprovalNotificationDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/ApprovalNotificationDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApprovalNotificationDto> kafkaListenerContainerFactoryForApproval() {
        ConcurrentKafkaListenerContainerFactory<String, ApprovalNotificationDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(approvalUpdateConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CategoryDto> categoryConsumerFactory() {
        JsonDeserializer<CategoryDto> jsonDeserializer = new JsonDeserializer<>(CategoryDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/CategoryDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CategoryDto> kafkaListenerContainerFactoryForCategory() {
        ConcurrentKafkaListenerContainerFactory<String, CategoryDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(categoryConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ProductDto> productConsumerFactory() {
        JsonDeserializer<ProductDto> jsonDeserializer = new JsonDeserializer<>(ProductDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/ProductDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductDto> kafkaListenerContainerFactoryForProduct() {
        ConcurrentKafkaListenerContainerFactory<String, ProductDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(productConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, VariantDto> variantConsumerFactory() {
        JsonDeserializer<VariantDto> jsonDeserializer = new JsonDeserializer<>(VariantDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/VariantDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, VariantDto> kafkaListenerContainerFactoryForVariant() {
        ConcurrentKafkaListenerContainerFactory<String, VariantDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(variantConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, InventoryDto> inventoryConsumerFactory() {
        JsonDeserializer<InventoryDto> jsonDeserializer = new JsonDeserializer<>(InventoryDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/InventoryDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InventoryDto> kafkaListenerContainerFactoryForInventory() {
        ConcurrentKafkaListenerContainerFactory<String, InventoryDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(inventoryConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PaymentDto> paymentConsumerFactory() {
        JsonDeserializer<PaymentDto> jsonDeserializer = new JsonDeserializer<>(PaymentDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/PaymentDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentDto> kafkaListenerContainerFactoryForPayment() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, TrackingDto> trackingConsumerFactory() {
        JsonDeserializer<TrackingDto> jsonDeserializer = new JsonDeserializer<>(TrackingDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/notificationservice/kafka/dtos/TrackingDto");
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
