package com.academy.projects.ecommerce.authenticationservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity(name = "permissions")
@Getter
@Setter
@RequiredArgsConstructor
@Transactional
public class Permission extends BaseModel implements Serializable {

    @Transient
    public static final String SEQUENCE_NAME = "permissions_sequence";

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Permission name should not be empty!!!")
    private String permissionName;

    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
}
