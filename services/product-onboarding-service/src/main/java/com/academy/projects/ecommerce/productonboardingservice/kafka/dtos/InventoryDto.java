package com.academy.projects.ecommerce.productonboardingservice.kafka.dtos;

import com.academy.projects.ecommerce.productonboardingservice.dtos.ActionType;
import com.academy.projects.ecommerce.productonboardingservice.models.InventoryUnit;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDto implements Serializable {
    private InventoryUnit inventoryUnit;
    private ActionType action;
}
