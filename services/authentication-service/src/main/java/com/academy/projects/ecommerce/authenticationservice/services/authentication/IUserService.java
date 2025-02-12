package com.academy.projects.ecommerce.authenticationservice.services.authentication;

import com.academy.projects.ecommerce.authenticationservice.kafka.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.authenticationservice.models.User;
import com.academy.projects.ecommerce.authenticationservice.models.UserState;

import java.util.Collection;
import java.util.List;

public interface IUserService {
    List<User> getAllUsers(int iPage, int iPageSize);
    User getUser(String email);
    User addRoles(String email, Collection<String> roles);
    void changeState(ApprovalRequest approvalRequest);
    void invalidate(String id);
    User updateState(String userId, UserState userState);
}
