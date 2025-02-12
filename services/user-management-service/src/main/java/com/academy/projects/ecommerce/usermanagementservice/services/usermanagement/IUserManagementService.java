package com.academy.projects.ecommerce.usermanagementservice.services.usermanagement;

import com.academy.projects.ecommerce.usermanagementservice.clients.dtos.SignUpRequestDto;
import com.academy.projects.ecommerce.usermanagementservice.dtos.UserValidityCheckResponseDto;
import com.academy.projects.ecommerce.usermanagementservice.models.User;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;
import com.academy.projects.ecommerce.usermanagementservice.models.UserType;

import java.util.List;

public interface IUserManagementService {
    boolean userExists(String email, UserType type);
    String registerInAuthenticationService(SignUpRequestDto requestDto);
    void updateState(String userId, UserState userState);
    User get(String userId);
    User save(User user);
    UserValidityCheckResponseDto isValid(String userId, UserType type);
    List<User> getUsers(int page, int pageSize);
}
