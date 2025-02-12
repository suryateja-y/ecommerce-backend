package com.academy.projects.ecommerce.approvalmanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalResponseDto implements Serializable {
    private String approvalId;
    private String message;
    private ResponseStatus responseStatus;
}
