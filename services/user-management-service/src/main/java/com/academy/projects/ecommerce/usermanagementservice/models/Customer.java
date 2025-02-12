package com.academy.projects.ecommerce.usermanagementservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Entity(name = "customers")
@Getter
@Setter
@RequiredArgsConstructor
public class Customer extends IDBaseModel {
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Address> addresses = new LinkedList<>();
}
