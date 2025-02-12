package com.academy.projects.ecommerce.notificationservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.LinkedList;
import java.util.List;

@Document(collection = "register")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Setting {
    @MongoId
    private String registryKey;
    @CustomUpdate
    private String description;
    @CustomUpdate
    private List<NotificationType> notificationTypes = new LinkedList<>();
    @CustomUpdate
    private EmailRegister emailRegister;
    @CustomUpdate
    private List<UserType> userTypes = new LinkedList<>();
    @CustomUpdate
    private boolean active = true;
}
