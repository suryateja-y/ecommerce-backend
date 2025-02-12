package com.academy.projects.ecommerce.notificationservice.repositories;

import com.academy.projects.ecommerce.notificationservice.models.NotificationType;
import com.academy.projects.ecommerce.notificationservice.models.Setting;
import com.academy.projects.ecommerce.notificationservice.models.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends MongoRepository<Setting, String> {
    boolean existsByRegistryKey(String registryKey);
    Optional<Setting> findByRegistryKey(String registryKey);
    Page<Setting> findAllByActiveAndNotificationTypesContainsAndUserTypesContains(boolean active, NotificationType notificationType, UserType userType, Pageable pageable);
    Page<Setting> findAllByActiveAndNotificationTypesContains(boolean active, NotificationType notificationType, Pageable pageable);
    Page<Setting> findAllByActiveAndUserTypesContains(boolean active, UserType userType, Pageable pageable);
    Page<Setting> findAllByActive(boolean active, Pageable pageable);
}
