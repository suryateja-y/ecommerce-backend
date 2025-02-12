package com.academy.projects.ecommerce.usermanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.usermanagementservice.models.User;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWrapperDto implements Serializable {
    private String id;
    private User user;
}
