package com.academy.projects.ecommerce.productsearchservice.kafka.consumer.services;

import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.AttributeDto;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.VariantActionDto;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.VariantContainer;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.VariantDto;
import com.academy.projects.ecommerce.productsearchservice.models.Variant;
import com.academy.projects.ecommerce.productsearchservice.services.IVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class VariantConsumer {
    private final IVariantService variantService;

    @Autowired
    public VariantConsumer(IVariantService variantService) {
        this.variantService = variantService;
    }
    @KafkaListener(topics = "${application.kafka.topics.variant-update-topic}", groupId = "${application.kafka.consumer.variant-update-group}", containerFactory = "kafkaListenerContainerFactoryForVariant")
    public void consumer(VariantDto variantDto) {
        try {
            variantService.update(from(variantDto));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private VariantActionDto from(VariantDto variantDto) {
        VariantActionDto variantActionDto = new VariantActionDto();
        variantActionDto.setAction(variantDto.getAction());
        variantActionDto.setVariant(from(variantDto.getVariant()));
        return variantActionDto;
    }

    private Variant from(VariantContainer variantContainer) {
        Variant variant = new Variant();
        variant.setVariantId(variantContainer.getVariantId());
        variant.setProductId(variantContainer.getProductId());
        variant.setVariantAttributes(from(variantContainer.getAttributes()));
        return variant;
    }

    private Map<String, String> from(List<AttributeDto> attributeDtos) {
        Map<String, String> attributes = new LinkedHashMap<>();
        for (AttributeDto attributeDto : attributeDtos)
            attributes.put(attributeDto.getName(), Objects.toString(attributeDto.getValue()));
        return attributes;
    }
}
