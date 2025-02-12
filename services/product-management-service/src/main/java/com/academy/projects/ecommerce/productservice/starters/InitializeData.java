package com.academy.projects.ecommerce.productservice.starters;

import com.academy.projects.ecommerce.productservice.models.*;
import com.academy.projects.ecommerce.productservice.repositories.CategoryRepository;
import com.academy.projects.ecommerce.productservice.repositories.ProductRepository;
import com.academy.projects.ecommerce.productservice.repositories.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final VariantRepository variantRepository;
    private boolean alreadySetup = false;

    @Autowired
    public InitializeData(CategoryRepository categoryRepository, ProductRepository productRepository, VariantRepository variantRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
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
        category.setEntityState(ApprovalStatus.APPROVED);
        category.setCategoryName("Mobile Phones");
        category.setCategoryDescription("Mobile Phones");
        category.setHighLevelCategory("Electronics");

        List<Attribute> attributes = new ArrayList<>();
        attributes.add(Attribute.builder().name("Is Smart Phone").dataType(DataType.BOOLEAN).build());
        attributes.add(Attribute.builder().name("Screen Size").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Front Camera Resolution").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Back Camera Resolution").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Battery Capacity").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("RAM Size").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Storage Capacity").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Warranty").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Screen Resolution").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("Color").dataType(DataType.STRING).build());
        attributes.add(Attribute.builder().name("In-Box Items").dataType(DataType.LIST).build());
        attributes.add(Attribute.builder().name("Made In India").dataType(DataType.BOOLEAN).build());
        category.setAttributes(attributes);
        categoryRepository.save(category);

        Product product = new Product();
        product.setCategory(category);
        product.setDescription("Mobile Phones");
        product.setName("iPhone");
        product.setDescription("Apple IPhone");
        product.setImage(List.of());
        product.setId("prod-f8dfbd8e-8390-427f-9c48-ab251b218153");
        product.setCompany("Academy Group Inc.");
        product.setDimensionalMetrics(new DimensionalMetrics());
        product.setBrandName("Academy");
        product.setApprovalStatus(ApprovalStatus.APPROVED);


        List<Attribute> productAttributes = new ArrayList<>();
        productAttributes.add(Attribute.builder().name("Is Smart Phone?").dataType(DataType.BOOLEAN).value(true).isRequired(true).build());
        productAttributes.add(Attribute.builder().name("Screen Size").dataType(DataType.STRING).value("6.5 Inch").isRequired(true).build());
        productAttributes.add(Attribute.builder().name("Front Camera Resolution").dataType(DataType.STRING).value("16MP").isRequired(true).build());
        productAttributes.add(Attribute.builder().name("Back Camera Resolution").dataType(DataType.STRING).value("50MP").isRequired(true).build());
        productAttributes.add(Attribute.builder().name("Battery Capacity").dataType(DataType.STRING).value("6000 mAh").isRequired(true).build());
        productAttributes.add(Attribute.builder().name("Warranty").dataType(DataType.STRING).value("2 Years").isRequired(true).build());
        productAttributes.add(Attribute.builder().name("Screen Resolution").dataType(DataType.STRING).value("1080p").isRequired(true).build());
        productAttributes.add(Attribute.builder().name("In-Box Items").dataType(DataType.LIST).value(List.of("In-Box Items", "Phone, Charging Cable, Adapter, Sim Ejector, Phone Cover, Warranty and User Guide, Bluetooth Headset")).isRequired(true).build());
        productAttributes.add(Attribute.builder().name("Made In India").dataType(DataType.BOOLEAN).value(true).isRequired(true).build());

        product.setAttributes(productAttributes);

        variantRepository.deleteAll();

        Variant variant = new Variant();
        variant.setId("vari-f8dfbd8e-8390-427f-9c48-ab251b218153");
        variant.setApprovalStatus(ApprovalStatus.APPROVED);
        variant.setProduct(product);
        List<Attribute> variantAttributes = new ArrayList<>();
        variantAttributes.add(Attribute.builder().name("RAM Size").dataType(DataType.STRING).value("32GB").isRequired(true).build());
        variantAttributes.add(Attribute.builder().name("Color").dataType(DataType.STRING).value("Black").isRequired(true).build());
        variantAttributes.add(Attribute.builder().name("Storage Capacity").dataType(DataType.STRING).value("128GB").isRequired(true).build());
        variant.setAttributes(variantAttributes);
        variantRepository.save(variant);

        Variant variant2 = new Variant();
        variant2.setId("vari-f8dfbd8e-8390-427f-9c48-ab251b218154");
        variant2.setApprovalStatus(ApprovalStatus.APPROVED);
        variant2.setProduct(product);
        List<Attribute> variant2Attributes = new ArrayList<>();
        variant2Attributes.add(Attribute.builder().name("RAM Size").dataType(DataType.STRING).value("16GB").isRequired(true).build());
        variant2Attributes.add(Attribute.builder().name("Color").dataType(DataType.STRING).value("Black").isRequired(true).build());
        variant2Attributes.add(Attribute.builder().name("Storage Capacity").dataType(DataType.STRING).value("128GB").isRequired(true).build());
        variant2.setAttributes(variant2Attributes);
        variantRepository.save(variant2);

        Variant variant3 = new Variant();
        variant3.setId("vari-f8dfbd8e-8390-427f-9c48-ab251b218154");
        variant3.setApprovalStatus(ApprovalStatus.APPROVED);
        variant3.setProduct(product);
        List<Attribute> variant3Attributes = new ArrayList<>();
        variant3Attributes.add(Attribute.builder().name("RAM Size").dataType(DataType.STRING).value("16GB").isRequired(true).build());
        variant3Attributes.add(Attribute.builder().name("Color").dataType(DataType.STRING).value("Black").isRequired(true).build());
        variant3Attributes.add(Attribute.builder().name("Storage Capacity").dataType(DataType.STRING).value("128GB").isRequired(true).build());
        variant3.setAttributes(variant3Attributes);
        variantRepository.save(variant3);

        product.setVariants(List.of(variant, variant2, variant3));
        productRepository.save(product);
        alreadySetup = true;
    }
}
