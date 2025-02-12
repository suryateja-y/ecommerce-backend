package com.academy.projects.ecommerce.usermanagementservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@Setter
public class FeignErrorResponseDto<T> implements Serializable {
    private T response;
    private HttpStatus httpStatus;
}
