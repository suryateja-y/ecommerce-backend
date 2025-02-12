package com.academy.projects.ecommerce.cartmanagementservice.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Getter
@Setter
public abstract class BaseModel {
    @MongoId
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
