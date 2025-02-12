package com.academy.projects.ecommerce.usermanagementservice.dtos;
import com.academy.projects.ecommerce.usermanagementservice.models.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRegistrationDto implements Serializable {
    @NotBlank(message = "Full name is required!!!")
    private String fullName;

    @NotBlank(message = "Email is required!!!")
    @Email(message = "Invalid Email!!!")
    private String email;

    @NotBlank(message = "Password should not be blank")
    private String password;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Age is required!!!")
    @Positive(message = "Age should not be a negative number!!!")
    private Integer age;

    @NotBlank(message = "Gender should be specified!!!")
    private Gender gender;

    @NotBlank(message = "Blood Group is required!!!")
    private String bloodGroup;
}
