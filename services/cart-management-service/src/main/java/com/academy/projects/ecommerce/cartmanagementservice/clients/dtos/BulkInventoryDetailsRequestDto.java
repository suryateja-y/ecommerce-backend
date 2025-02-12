package com.academy.projects.ecommerce.cartmanagementservice.clients.dtos;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkInventoryDetailsRequestDto implements Serializable {
    private List<InventoryDetailsRequestDto> requests;
}
