package com.academy.projects.ecommerce.usermanagementservice.clients.dtos;

import com.academy.projects.ecommerce.usermanagementservice.models.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto implements Serializable {
    @NotBlank(message = "Email is required!!!")
    @Email(message = "Invalid Email!!!")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    private UserType userType;
}
