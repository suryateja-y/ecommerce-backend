package com.academy.projects.ecommerce.productonboardingservice.kafka.dtos;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.models.Variant;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantDto implements Serializable {
    private Variant variant;
    private String sellerId;
    private ActionType action;
}
