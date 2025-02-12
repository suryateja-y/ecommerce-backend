package com.academy.projects.ecommerce.usermanagementservice.kafka.producer.user;

import com.academy.projects.ecommerce.usermanagementservice.models.User;

public interface IUserUpdateManager {
    void sendUserUpdate(User user, String approvalId, String updaterId);
}
