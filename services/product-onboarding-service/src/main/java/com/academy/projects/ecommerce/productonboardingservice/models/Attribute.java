package com.academy.projects.ecommerce.productonboardingservice.models;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attribute {
    private String name;
    private Object value;
    private DataType dataType;
    @Builder.Default
    private boolean isRequired = true;

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;
        if((object == null) || (getClass() != object.getClass())) return false;
        Attribute attribute = (Attribute) object;
        return ((this.name.equals(attribute.name)) && (Objects.deepEquals(this.value, attribute.value)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }
}
