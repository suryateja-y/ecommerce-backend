package com.academy.projects.ecommerce.notificationservice.repositories;

import com.academy.projects.ecommerce.notificationservice.models.User;
import com.academy.projects.ecommerce.notificationservice.models.UserState;
import com.academy.projects.ecommerce.notificationservice.models.UserType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByEmail(String email);
    List<User> findAllByUserTypeAndUserState(UserType userType, UserState userState);
    boolean existsByUserId(String user);
}
