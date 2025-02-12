package com.academy.projects.ecommerce.productsearchservice.kafka.consumer.configurations;

import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.InventoryUnitDto;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.ProductDto;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.SellerDto;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.VariantDto;
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

    @Value("${application.kafka.host}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, InventoryUnitDto> inventoryConsumerFactory() {
        JsonDeserializer<InventoryUnitDto> jsonDeserializer = new JsonDeserializer<>(InventoryUnitDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/productsearchservice/kafka/dtos/InventoryUnitDto");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InventoryUnitDto> kafkaListenerContainerFactoryForInventory() {
        ConcurrentKafkaListenerContainerFactory<String, InventoryUnitDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(inventoryConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ProductDto> productConsumerFactory() {
        JsonDeserializer<ProductDto> jsonDeserializer = new JsonDeserializer<>(ProductDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/productsearchservice/kafka/dtos/ProductDto.java");
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
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/productsearchservice/kafka/dtos/VariantDto.java");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, VariantDto> kafkaListenerContainerFactoryForVariant() {
        ConcurrentKafkaListenerContainerFactory<String, VariantDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(variantConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, SellerDto> sellerConsumerFactory() {
        JsonDeserializer<SellerDto> jsonDeserializer = new JsonDeserializer<>(SellerDto.class);
        jsonDeserializer.addTrustedPackages("com/academy/projects/ecommerce/productsearchservice/kafka/dtos/SellerDto.java");
        return new DefaultKafkaConsumerFactory<>(configurationProperties(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SellerDto> kafkaListenerContainerFactoryForSeller() {
        ConcurrentKafkaListenerContainerFactory<String, SellerDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(sellerConsumerFactory());
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
