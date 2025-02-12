package com.academy.projects.ecommerce.authenticationservice.dtos.authentication;

import com.academy.projects.ecommerce.authenticationservice.models.UserType;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class SignUpRequestDto implements Serializable {
    @Email(message = "Invalid Email!!!")
    private String email;
    private String password;
    private UserType userType;
}
