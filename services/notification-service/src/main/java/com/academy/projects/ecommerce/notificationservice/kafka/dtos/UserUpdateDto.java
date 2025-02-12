package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import com.academy.projects.ecommerce.notificationservice.models.User;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto implements Serializable {
    private Action action;
    private User user;
    private String approvalId;
    private String updaterId;
}
