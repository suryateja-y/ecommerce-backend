package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerUpdateDto implements Serializable {
    private Customer customer;
    private Action action;
}
