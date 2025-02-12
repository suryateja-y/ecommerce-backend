package com.academy.projects.ecommerce.usermanagementservice.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity(name = "users")
@RequiredArgsConstructor
public class User extends IDBaseModel {
    private String fullName;

    @CustomUpdate
    private String email;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @CustomUpdate
    private UserState userState = UserState.PENDING_FOR_APPROVAL;

    @Enumerated(EnumType.STRING)
    @CustomUpdate
    private UserType userType;
}
