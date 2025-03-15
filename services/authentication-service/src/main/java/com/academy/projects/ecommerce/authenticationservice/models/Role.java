package com.academy.projects.ecommerce.authenticationservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity(name = "roles")
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
public class Role extends BaseModel implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "roles_sequence";

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Role name should not be empty!!!")
    private String roleName;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
