package com.academy.projects.ecommerce.productonboardingservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "images")
public class Image extends BaseModel {
    @Transient
    public static final String SEQUENCE_NAME = "image_sequence";
    private String url;
    private String format;
}
