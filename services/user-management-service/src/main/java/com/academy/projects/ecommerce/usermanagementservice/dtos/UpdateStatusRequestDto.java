package com.academy.projects.ecommerce.usermanagementservice.dtos;

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
public class UpdateStatusRequestDto implements Serializable {
    private UserState userState;
}
