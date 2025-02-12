package com.academy.projects.ecommerce.inventorymanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.inventorymanagementservice.models.InventoryUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUnitDto implements Serializable {
    private InventoryUnit inventoryUnit;
    private Action action;
}
