package com.academy.projects.ecommerce.approvalmanagementservice.configurations;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("accessChecker")
public class AccessChecker {
    public boolean isOwner(String userId) {
        return SecurityContextHolder.getContext().getAuthentication().getName().equals(userId);
    }
}
