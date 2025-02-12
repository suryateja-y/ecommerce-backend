package com.academy.projects.ecommerce.inventorymanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BulkInventoryDetailsRequestDto implements Serializable {
    private List<InventoryDetailsRequestDto> requests;
}
