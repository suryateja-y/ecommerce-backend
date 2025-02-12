package com.academy.projects.ecommerce.productonboardingservice.dtos;

import com.academy.projects.ecommerce.productonboardingservice.models.InternalResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class InternalResponseDto implements Serializable {
    private String message;
    private InternalResponseStatus responseStatus;
    private Date respondedAt;
    private String id;
}
