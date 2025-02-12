package com.academy.projects.ecommerce.productonboardingservice.kafka.dtos;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.models.Category;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto implements Serializable {
    private ActionType action;
    private Category category;
}
