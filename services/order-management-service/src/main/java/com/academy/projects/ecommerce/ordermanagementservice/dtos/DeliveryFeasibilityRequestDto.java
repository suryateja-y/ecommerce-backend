package com.academy.projects.ecommerce.ordermanagementservice.dtos;

import com.academy.projects.ecommerce.ordermanagementservice.models.Address;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryFeasibilityRequestDto implements Serializable {
    private List<DeliveryFeasibilityItem> items;
    private Address customerAddress;
}
