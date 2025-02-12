package com.academy.projects.ecommerce.productonboardingservice.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "high-level-categories")
@Getter
@Setter
@RequiredArgsConstructor
public class HighLevelCategory extends BaseModel {
    @Indexed(unique = true)
    private String highLevelCategory;
}
