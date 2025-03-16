package com.academy.projects.ecommerce.authenticationservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@Transactional
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueEmailAndUserType", columnNames = {"email", "userType"})})
public class User extends BaseModel implements Serializable {
    @Transient
    public static final String SEQUENCE_NAME = "User_Sequence";

    @Column(nullable = false)
    @NotBlank(message = "Email should not be empty!!!")
    @Email(message = "Invalid Email!!!")
    private String email;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserState userState = UserState.PENDING_FOR_APPROVAL;

    @JsonIgnore
    @Column(nullable = false)
    @NotBlank(message = "Password should not be empty!!!")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
