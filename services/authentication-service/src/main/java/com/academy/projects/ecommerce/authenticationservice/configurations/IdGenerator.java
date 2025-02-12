package com.academy.projects.ecommerce.authenticationservice.configurations;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdGenerator {
    public String getId(String sequenceName) {
        sequenceName = sequenceName.length() > 4 ? sequenceName.substring(0, 4).toLowerCase() : sequenceName.toLowerCase();
        return sequenceName + "-" + UUID.randomUUID();
    }
}
