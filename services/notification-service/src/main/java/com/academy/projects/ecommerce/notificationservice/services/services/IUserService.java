package com.academy.projects.ecommerce.notificationservice.services.services;

import com.academy.projects.ecommerce.notificationservice.models.User;
import com.academy.projects.ecommerce.notificationservice.models.UserState;
import com.academy.projects.ecommerce.notificationservice.models.UserType;

import java.util.List;

public interface IUserService {
    void save(User user);
    String getUserId(String email);
    User getByUserid(String requester);
    List<User> get(UserType userType, UserState userState);
}
