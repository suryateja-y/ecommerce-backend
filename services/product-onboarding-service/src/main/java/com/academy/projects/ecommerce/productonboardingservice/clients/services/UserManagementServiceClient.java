package com.academy.projects.ecommerce.productonboardingservice.clients.services;

import com.academy.projects.ecommerce.productonboardingservice.clients.dtos.UserType;
import com.academy.projects.ecommerce.productonboardingservice.clients.dtos.UserValidityCheckResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${application.services.user-management-service}")
public interface UserManagementServiceClient {
    @PostMapping("/api/${application.version}/users/isValid")
    UserValidityCheckResponseDto isUserValid(@RequestBody UserType userType);
}
