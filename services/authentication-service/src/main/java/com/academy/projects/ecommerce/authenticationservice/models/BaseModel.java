package com.academy.projects.ecommerce.authenticationservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public abstract class BaseModel implements Serializable {

    @Id
    private String id;

    @CreatedDate
    private Date createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Date modifiedAt;

    @LastModifiedBy
    private String modifiedBy;
}
