package com.academy.projects.ecommerce.usermanagementservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IDBaseModel extends BaseModel {
    @Id
    @Column(updatable = false, nullable = false)
    @CustomUpdate
    private String id;
}
