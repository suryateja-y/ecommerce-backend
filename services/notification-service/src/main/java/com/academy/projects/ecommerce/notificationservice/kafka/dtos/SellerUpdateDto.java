package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerUpdateDto implements Serializable {
    private Action action;
    private Seller seller;
}
