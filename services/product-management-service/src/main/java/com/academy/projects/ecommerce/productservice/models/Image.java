package com.academy.projects.ecommerce.productservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image extends BaseModel {
    private String url;
    private String format;
}
