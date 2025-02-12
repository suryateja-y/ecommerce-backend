package com.academy.projects.ecommerce.productservice.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    private String name;
    private Object value;
    private DataType dataType;
    @Builder.Default
    private boolean isRequired = true;
}
