package com.academy.projects.ecommerce.authenticationservice.controllers.authentication;

import com.academy.projects.ecommerce.authenticationservice.dtos.authentication.*;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authentication.UserAlreadyExistsException;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authentication.UserNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.services.authentication.IAuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/${application.version}/authentication")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @Autowired
    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // Should not be a public API
    @PostMapping("/register")
    public ResponseEntity<SignUpResponseDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto responseDto = new SignUpResponseDto();
        try {
            responseDto = this.authenticationService.signup(signUpRequestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            responseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
        } catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public AuthenticationResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String token = this.authenticationService.login(loginRequestDto);
        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setEmail(loginRequestDto.getEmail());
        authenticationResponseDto.setToken(token);
        return authenticationResponseDto;
    }

    @GetMapping("/isValid")
    public UserPermissionsDto isTokenValid(@RequestParam String token) {
        return authenticationService.validateAndGetRoles(token);
    }

    @ExceptionHandler({InternalAuthenticationServiceException.class, BadCredentialsException.class, AuthenticationException.class, UserAlreadyExistsException.class})
    public ResponseEntity<String> handleAuthenticationFailure(InternalAuthenticationServiceException e) {
        logger.error(e.getMessage());
        if((e.getCause() instanceof UserNotFoundException) || (e.getCause() instanceof BadCredentialsException) || (e.getCause() instanceof AuthenticationException)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } else if(e.getCause() instanceof UserAlreadyExistsException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } else
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
