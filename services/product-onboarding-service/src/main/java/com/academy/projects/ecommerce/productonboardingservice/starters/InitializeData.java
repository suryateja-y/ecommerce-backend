package com.academy.projects.ecommerce.productonboardingservice.starters;

import com.academy.projects.ecommerce.productonboardingservice.models.*;
import com.academy.projects.ecommerce.productonboardingservice.repositories.CategoryRepository;
import com.academy.projects.ecommerce.productonboardingservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private boolean alreadySetup = false;

    @Autowired
    public InitializeData(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @SuppressWarnings({"NullableProblems", "DuplicatedCode"})
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // Creating Category
        Category category = new Category();
        category.setId("cate-f8dfbd8e-8390-427f-9c48-ab251b218153");
        category.setApprovalStatus(ApprovalStatus.APPROVED);
        category.setCategoryName("Mobile Phones");
        category.setCategoryDescription("Mobile Phones");
        category.setHighLevelCategory("Electronics");

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(Attribute.builder().name("Is Smart Phone").dataType(DataType.BOOLEAN).build());
        attributes.add(Attribute.builder().name("Screen Size").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Front Camera Resolution").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Back Camera Resolution").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Battery Capacity").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Warranty").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Screen Resolution").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("In-Box Items").dataType(DataType.LIST).build());
        attributes.add(Attribute.builder().name("Made In India").dataType(DataType.BOOLEAN).build());
        category.setAttributes(attributes);

        List<Attribute> variantAttributes = new ArrayList<>();
        variantAttributes.add(Attribute.builder().name("RAM Size").dataType(DataType.STRING).build());
        variantAttributes.add(Attribute.builder().name("Storage Capacity").dataType(DataType.STRING).build());
        variantAttributes.add(Attribute.builder().name("Color").dataType(DataType.STRING).build());
        category.setVariantAttributes(variantAttributes);

        categoryRepository.save(category);

        Product product = new Product();
        product.setApprovalStatus(ApprovalStatus.APPROVED);
        product.setCategory(category);
        product.setName("Apple Mac Book Pro");
        product.setDescription("Apple Mac Book Pro");
        product.setPrice(BigDecimal.valueOf(200000));
        product.setBrandName("Apple");
        product.setCompany("Apple Inc.");
        productRepository.save(product);

        alreadySetup = true;
    }
}
