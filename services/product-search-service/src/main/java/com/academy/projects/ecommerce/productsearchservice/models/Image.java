package com.academy.projects.ecommerce.productsearchservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image extends BaseModel {
    @Transient
    public static final String SEQUENCE_NAME = "image_sequence";
    private String url;
    private String format;
}
