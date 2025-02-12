package com.academy.projects.ecommerce.notificationservice.services.services;

import com.academy.projects.ecommerce.notificationservice.configurations.Patcher;
import com.academy.projects.ecommerce.notificationservice.exceptions.UserNotFoundException;
import com.academy.projects.ecommerce.notificationservice.models.User;
import com.academy.projects.ecommerce.notificationservice.models.UserState;
import com.academy.projects.ecommerce.notificationservice.models.UserType;
import com.academy.projects.ecommerce.notificationservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final Patcher patcher;

    @Autowired
    public UserService(UserRepository userRepository, Patcher patcher) {
        this.userRepository = userRepository;
        this.patcher = patcher;
    }

    @Override
    public void save(User user) {
        User savedUser = userRepository.findByUserId(user.getUserId()).orElse(new User());
        patcher.entity(savedUser, user, User.class);
        userRepository.save(savedUser);
    }

    @Override
    public String getUserId(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        return user.getUserId();
    }

    @Override
    public User getByUserid(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<User> get(UserType userType, UserState userState) {
        return userRepository.findAllByUserTypeAndUserState(userType, userState);
    }
}
