package com.academy.projects.ecommerce.usermanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.usermanagementservice.dtos.ActionType;
import com.academy.projects.ecommerce.usermanagementservice.models.Seller;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDto implements Serializable {
    private Seller seller;
    private ActionType action;
}
