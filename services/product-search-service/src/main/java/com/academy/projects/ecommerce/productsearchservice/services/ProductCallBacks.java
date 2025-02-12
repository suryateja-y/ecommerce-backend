package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.productsearchservice.dtos.ProductFilters;
import com.academy.projects.ecommerce.productsearchservice.models.Product;
import com.academy.projects.ecommerce.productsearchservice.models.Variant;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.event.BeforeConvertCallback;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("NullableProblems")
@Service
@Getter
public class ProductCallBacks implements BeforeConvertCallback<Product> {

    private final IdGenerator idGenerator;

    @Autowired
    public ProductCallBacks(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public Product onBeforeConvert(Product product, IndexCoordinates index) {
        if(product.getProductId() == null)
            product.setProductId(idGenerator.getId(Product.SEQUENCE_NAME));


        Set<String> searchAttributes = new LinkedHashSet<>(getFieldNames());
        if(product.getAttributes() != null)
            searchAttributes.addAll(product.getAttributes().keySet());
        if(product.getVariants() != null) {
            for(Variant variant : product.getVariants()) {
                if(variant.getVariantAttributes() != null)
                    searchAttributes.addAll(variant.getVariantAttributes().keySet());
            }
        }
        product.setSearchAttributes(searchAttributes);
        return product;
    }

    private List<String> getFieldNames() {
        List<String> fieldNames = new LinkedList<>();
        Class<ProductFilters> productFiltersClass = ProductFilters.class;
        Field[] fields = productFiltersClass.getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().equalsIgnoreCase("attributes") || field.getName().equalsIgnoreCase("variantAttributes")) continue;
            field.setAccessible(true);
            fieldNames.add(field.getName());
            field.setAccessible(false);
        }
        return fieldNames;
    }
}
