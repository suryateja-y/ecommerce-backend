package com.academy.projects.ecommerce.usermanagementservice.clients.services;

import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.SignUpRequestDto;
import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.SignUpResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.models.User;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${application.services.authentication-service}")
public interface AuthenticationServiceClient {
    @PostMapping("/api/${application.version}/authentication/register")
    SignUpResponseDto register(@Valid @RequestBody SignUpRequestDto signUpRequestDto);

    @DeleteMapping("/api/${application.version}/authorization/users/{id}")
    void invalidate(@PathVariable String id);

    @PutMapping("/api/${application.version}/authorization/users/{userId}")
    User updateState(@PathVariable String userId, @RequestBody UserState userState);
}
