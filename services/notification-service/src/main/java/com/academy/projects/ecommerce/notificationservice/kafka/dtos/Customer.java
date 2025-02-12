package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import com.academy.projects.ecommerce.notificationservice.models.User;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Customer implements Serializable {
    private String id;
    private String approvalId;
    private User user;
}
