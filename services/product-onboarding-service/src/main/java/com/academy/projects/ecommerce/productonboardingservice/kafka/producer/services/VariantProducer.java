package com.academy.projects.ecommerce.productonboardingservice.kafka.producer.services;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.kafka.dtos.VariantDto;
import com.academy.projects.ecommerce.productonboardingservice.models.Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class VariantProducer {
    @Value("${application.kafka.topics.variant-topic}")
    private String variantTopic;

    private final KafkaTemplate<String, VariantDto> variantKafkaTemplate;

    @Autowired
    public VariantProducer(KafkaTemplate<String, VariantDto> variantKafkaTemplate) {
        this.variantKafkaTemplate = variantKafkaTemplate;
    }

    public void send(VariantDto variantDto) {
        variantKafkaTemplate.send(variantTopic, variantDto);
    }

    public void send(Variant variant, String sellerId, ActionType actionType) {
        VariantDto variantDto = VariantDto.builder().variant(variant).sellerId(sellerId).action(actionType).build();
        variantKafkaTemplate.send(variantTopic, variantDto);
    }
}
