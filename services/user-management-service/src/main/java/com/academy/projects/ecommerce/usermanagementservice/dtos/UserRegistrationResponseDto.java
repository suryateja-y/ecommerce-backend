package com.academy.projects.ecommerce.usermanagementservice.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class UserRegistrationResponseDto implements Serializable {
    private String userId;
    private Date createdOrModifiedAt;
    private String message;
}
