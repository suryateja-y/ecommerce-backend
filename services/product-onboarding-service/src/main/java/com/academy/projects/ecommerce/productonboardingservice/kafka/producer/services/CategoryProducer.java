package com.academy.projects.ecommerce.productonboardingservice.kafka.producer.services;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.kafka.dtos.CategoryDto;
import com.academy.projects.ecommerce.productonboardingservice.models.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CategoryProducer {
    @Value("${application.kafka.topics.category-topic}")
    private String categoryTopic;

    private final KafkaTemplate<String, CategoryDto> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(CategoryProducer.class);

    @Autowired
    public CategoryProducer(KafkaTemplate<String, CategoryDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Category category, ActionType actionType) {
        CategoryDto categoryDto = CategoryDto.builder().category(category).action(actionType).build();
        kafkaTemplate.send(categoryTopic, categoryDto);
        logger.info("Sent Category Update: {}", categoryDto.getCategory().getCategoryName());
    }
}
