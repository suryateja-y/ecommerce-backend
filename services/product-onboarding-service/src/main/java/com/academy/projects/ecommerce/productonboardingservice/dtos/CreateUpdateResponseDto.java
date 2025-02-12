package com.academy.projects.ecommerce.productonboardingservice.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class CreateUpdateResponseDto implements Serializable {
    private String message;
    private Date createdOrUpdatedDate;
    private String id;
}
