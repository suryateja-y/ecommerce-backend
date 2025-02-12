package com.academy.projects.ecommerce.notificationservice.kafka.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    private String id;
    private String categoryName;
    private String highLevelCategory;
    private String description;
}
