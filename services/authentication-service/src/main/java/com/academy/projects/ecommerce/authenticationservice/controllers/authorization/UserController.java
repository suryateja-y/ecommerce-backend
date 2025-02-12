package com.academy.projects.ecommerce.authenticationservice.controllers.authorization;

import com.academy.projects.ecommerce.authenticationservice.exceptions.authentication.UserNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.RoleNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.models.User;
import com.academy.projects.ecommerce.authenticationservice.models.UserState;
import com.academy.projects.ecommerce.authenticationservice.services.authentication.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/${application.version}/authorization/users")
public class UserController {

    private final IUserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(final IUserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('CRUD_USER')")
    public ResponseEntity<Void> invalidate(@PathVariable final String userId) {
        userService.invalidate(userId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CRUD_USER')")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<User> users = userService.getAllUsers(page, pageSize);
        return ResponseEntity.ok(users);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<User> getUserDetails(Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('CRUD_USER') or (hasRole('ROLE_USER') and @accessChecker.isOwner(#userId))")
    public ResponseEntity<User> getUserDetails(@PathVariable String userId) {
        User user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('CRUD_USER')")
    public ResponseEntity<User> addRoleToUser(@RequestBody List<String> roles, @PathVariable String userId) {
        User user = userService.addRoles(userId, roles);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('CRUD_USER') or hasAnyRole('ROLE_SELLER_MANAGER', 'ROLE_CUSTOMER_MANAGER', 'ROLE_EMPLOYEE_MANAGER')")
    public ResponseEntity<User> updateState(@PathVariable String userId, @RequestBody UserState userState) {
        User user = userService.updateState(userId, userState);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
    public ResponseEntity<String> handleUserNotFoundException(final RuntimeException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
