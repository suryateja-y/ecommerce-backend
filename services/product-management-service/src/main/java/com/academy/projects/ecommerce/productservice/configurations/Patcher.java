package com.academy.projects.ecommerce.productservice.configurations;

import com.academy.projects.ecommerce.productservice.models.CustomUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class Patcher {
    private final Logger logger = LoggerFactory.getLogger(Patcher.class);

    public <T> void entity(T saved, T requested, Class<?> className) {
        try {
            Field[] categoryFields = className.getDeclaredFields();
            for (Field categoryField : categoryFields) {
                if (categoryField.isAnnotationPresent(CustomUpdate.class)) continue;
                categoryField.setAccessible(true);
                Object value = categoryField.get(requested);
                if (value != null) {
                    categoryField.set(saved, value);
                }
                categoryField.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }
}
