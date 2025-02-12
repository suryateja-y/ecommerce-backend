package com.academy.projects.ecommerce.productservice.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Getter
@Setter
public abstract class BaseModel {
    @Id
    @MongoId
    private String id;

    private Date createdDate;
    private Date modifiedDate;
}
