package com.academy.projects.ecommerce.authenticationservice.dtos.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class SignUpResponseDto implements Serializable {
    private String userId;
    private String message;
    private Date createdAt;
}
