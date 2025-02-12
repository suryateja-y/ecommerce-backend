package com.academy.projects.ecommerce.ordermanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.ordermanagementservice.models.InventoryUnit;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryUnitDto implements Serializable {
    private InventoryUnit inventoryUnit;
    private Action action;
}
