package com.academy.projects.ecommerce.productservice.kafka.consumer.services;

import com.academy.projects.ecommerce.productservice.kafka.dtos.VariantDto;
import com.academy.projects.ecommerce.productservice.models.ApprovalStatus;
import com.academy.projects.ecommerce.productservice.models.Variant;
import com.academy.projects.ecommerce.productservice.services.IVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class VariantConsumer {
    private final IVariantService variantService;

    @Autowired
    public VariantConsumer(final IVariantService variantService) {
        this.variantService = variantService;
    }

    @KafkaListener(topics = "${application.kafka.topics.variant-topic}", groupId = "${application.kafka.consumer.variant-group}", containerFactory = "variantKafkaListenerContainerFactory")
    public void consume(VariantDto variantDto) {
        Variant variant = variantDto.getVariant();
        if(variant.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
            variantService.consumeVariant(variant);
        } else {
            variantService.deleteVariant(variant);
        }
    }
}
