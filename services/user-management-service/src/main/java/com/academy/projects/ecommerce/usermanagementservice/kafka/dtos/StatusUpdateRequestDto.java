package com.academy.projects.ecommerce.usermanagementservice.kafka.dtos;

import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequestDto implements Serializable {
    private String approvalId;
    private UserState approvalStatus;
    private String requesterId;
}
