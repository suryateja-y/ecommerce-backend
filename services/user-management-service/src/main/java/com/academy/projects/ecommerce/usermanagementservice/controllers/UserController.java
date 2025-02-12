package com.academy.projects.ecommerce.usermanagementservice.controllers;

import com.academy.projects.ecommerce.usermanagementservice.dtos.UserValidityCheckResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.models.User;
import com.academy.projects.ecommerce.usermanagementservice.models.UserType;
import com.academy.projects.ecommerce.usermanagementservice.services.usermanagement.IUserManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/${application.version}/users")
public class UserController {

    private final IUserManagementService userManagementService;

    public UserController(final IUserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping("/isValid")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserValidityCheckResponseDto> isValid(Authentication authentication, @RequestBody UserType userType) {
        UserValidityCheckResponseDto responseDto = new UserValidityCheckResponseDto();
        try {
            responseDto = userManagementService.isValid(authentication.getName(), userType);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception exception) {
            responseDto.setValid(false);
            responseDto.setMessage(exception.getMessage());
            return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<User> getUserDetails(Authentication authentication) {
        User user = userManagementService.get(authentication.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('CRUD_USER')")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User user = userManagementService.get(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CRUD_USER')")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<User> users = userManagementService.getUsers(page, pageSize);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
