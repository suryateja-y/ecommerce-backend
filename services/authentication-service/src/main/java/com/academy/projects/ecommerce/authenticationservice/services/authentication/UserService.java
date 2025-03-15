package com.academy.projects.ecommerce.authenticationservice.services.authentication;

import com.academy.projects.ecommerce.authenticationservice.exceptions.authentication.UserNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.exceptions.authorization.RoleNotFoundException;
import com.academy.projects.ecommerce.authenticationservice.kafka.dtos.ApprovalRequest;
import com.academy.projects.ecommerce.authenticationservice.kafka.dtos.ApprovalStatus;
import com.academy.projects.ecommerce.authenticationservice.models.Role;
import com.academy.projects.ecommerce.authenticationservice.models.User;
import com.academy.projects.ecommerce.authenticationservice.models.UserState;
import com.academy.projects.ecommerce.authenticationservice.repositories.authentication.UserRepository;
import com.academy.projects.ecommerce.authenticationservice.repositories.authorization.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(final UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public List<User> getAllUsers(int iPage, int iPageSize) {
        Pageable pageable = PageRequest.of(iPage, iPageSize);
        Page<User> page = userRepository.findAll(pageable);
        return page.get().toList();
    }

    @Override
    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public User addRoles(String userId, Collection<String> roles) {
        User user = getUser(userId);
        List<Role> roleList = mapRoles(roles);
        user.getRoles().addAll(roleList);
        logger.info("Roles: {} has been added to the User: '{}'!!!", roleList.stream().map(Role::getRoleName).toList(), user.getId());

        // Send Notification to the User and User Manager
        return userRepository.save(user);
    }

    private List<Role> mapRoles(Collection<String> roles) {
        List<Role> rolesList = new ArrayList<>();
        for (String role : roles) {
            Role savedRole = roleRepository.findByRoleName(role).orElseThrow(() -> new RoleNotFoundException(role));
            rolesList.add(savedRole);
        }
        return rolesList;
    }

    @Override
    @CacheEvict(value = "users", key = "#approvalRequest.requester")
    public void changeState(ApprovalRequest approvalRequest) {
        User user = getUser(approvalRequest.getRequester());
        user.setUserState(from(approvalRequest.getStatus()));
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public void invalidate(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setUserState(UserState.REJECTED);
        user.setEnabled(false);
        userRepository.save(user);

        logger.info("User: '{}' has been blocked!!!", user.getEmail());
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public User updateState(String userId, UserState userState) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.setUserState(userState);
        if((userState == UserState.IN_ACTIVE) || (userState == UserState.REJECTED))
            user.setEnabled(false);
        user = userRepository.save(user);
        logger.info("User: '{}' has been updated to the state: '{}'!!!", user.getEmail(), userState);
        return user;
    }

    private UserState from(ApprovalStatus approvalStatus) {
        if(approvalStatus == ApprovalStatus.APPROVED) return UserState.APPROVED;
        if(approvalStatus == ApprovalStatus.REJECTED) return UserState.REJECTED;
        return UserState.PENDING_FOR_APPROVAL;
    }
}
