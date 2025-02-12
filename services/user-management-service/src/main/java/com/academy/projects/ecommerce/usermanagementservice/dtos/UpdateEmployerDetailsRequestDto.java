package com.academy.projects.ecommerce.usermanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployerDetailsRequestDto implements Serializable {
    private String employeeId;
    private String designation;
}
