package com.academy.projects.trackingmanagementservice.clients.configuration;

import com.academy.projects.trackingmanagementservice.dtos.FeignErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FeignResponseDecoder {

    private final Logger logger = LoggerFactory.getLogger(FeignResponseDecoder.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public FeignResponseDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> FeignErrorResponseDto<T> decode(Exception exception, Class<T> className) {
        try {
            FeignException feignException = (FeignException) exception;
            String content = feignException.contentUTF8();
            FeignErrorResponseDto<T> responseDto = new FeignErrorResponseDto<>();
            responseDto.setResponse(objectMapper.readValue(content, className));
            responseDto.setHttpStatus(HttpStatus.valueOf(feignException.status()));
            return responseDto;
        } catch(IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
