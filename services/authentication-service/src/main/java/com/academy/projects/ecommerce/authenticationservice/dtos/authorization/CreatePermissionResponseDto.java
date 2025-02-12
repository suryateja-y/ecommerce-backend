package com.academy.projects.ecommerce.authenticationservice.dtos.authorization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class CreatePermissionResponseDto implements Serializable {
    private Date createdAt;
}
