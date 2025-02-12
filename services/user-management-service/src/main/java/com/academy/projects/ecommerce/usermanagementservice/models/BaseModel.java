package com.academy.projects.ecommerce.usermanagementservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {
    @CreatedDate
    @CustomUpdate
    private Date createdAt;

    @CreatedBy
    @CustomUpdate
    private String createdBy;

    @LastModifiedDate
    @CustomUpdate
    private Date modifiedAt;

    @LastModifiedBy
    @CustomUpdate
    private String modifiedBy;
}
