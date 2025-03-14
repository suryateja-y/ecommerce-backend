package com.academy.projects.ecommerce.productsearchservice.starters;

import com.academy.projects.ecommerce.productsearchservice.models.*;
import com.academy.projects.ecommerce.productsearchservice.repositories.InventoryRepository;
import com.academy.projects.ecommerce.productsearchservice.repositories.ProductRepository;
import com.academy.projects.ecommerce.productsearchservice.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InitializeData(ProductRepository productRepository, SellerRepository sellerRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @SuppressWarnings({"NullableProblems", "DuplicatedCode", "ExtractMethodRecommender"})
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;
        productRepository.deleteAll();

        // Mobile Phone
        Category mobiles = new Category();
        mobiles.setId(GlobalData.CATEGORY_ID);
        mobiles.setCategoryName("Mobile Phones");
        mobiles.setCategoryDescription("Mobiles");
        mobiles.setHighLevelCategory("Electronics");

        Product product = new Product();
        product.setName("iPhone");
        product.setDescription("Apple IPhone");
        product.setImage(List.of());
        product.setId(GlobalData.PRODUCT_ID);
        product.setCompany("Academy Group Inc.");
        product.setProductId(GlobalData.PRODUCT_ID);
        product.setDimensionalMetrics(new DimensionalMetrics());
        product.setCategory(mobiles);
        product.setBrandName("Academy");

        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put("Is Smart Phone?", "true");
        attributes.put("Screen Size", "6.5 Inch");
        attributes.put("Front Camera Resolution", "16 MP");
        attributes.put("Back Camera Resolution", "50 MP");
        attributes.put("Battery Capacity", "6000 mAh");
        attributes.put("Warranty", "2 Years");
        attributes.put("Screen Resolution", "1080p");
        attributes.put("In-Box Items", "Phone, Charging Cable, Adapter, Sim Ejector, Phone Cover, Warranty and User Guide, Bluetooth Headset");
        attributes.put("Made In India", "true");

        product.setAttributes(attributes);

        Variant variant1 = new Variant();
        variant1.setProductId(GlobalData.PRODUCT_ID);
        variant1.setVariantId(GlobalData.VARIANT1_ID);
        Map<String, String> variantAttributes = new LinkedHashMap<>();
        variantAttributes.put("RAM Size", "32GB");
        variantAttributes.put("Storage Capacity", "128GB");
        variantAttributes.put("Color", "Black");
        variant1.setVariantAttributes(variantAttributes);
        product.getVariants().add(variant1);

        Variant variant2 = new Variant();
        variant2.setProductId(GlobalData.PRODUCT_ID);
        variant2.setVariantId(GlobalData.VARIANT2_ID);
        Map<String, String> variant2Attributes = new LinkedHashMap<>();
        variant2Attributes.put("RAM Size", "16GB");
        variant2Attributes.put("Storage Capacity", "128GB");
        variant2Attributes.put("Color", "Black");
        variant2.setVariantAttributes(variant2Attributes);
        product.getVariants().add(variant2);

        Variant variant3 = new Variant();
        variant3.setProductId(GlobalData.PRODUCT_ID);
        variant3.setVariantId(GlobalData.VARIANT3_ID);
        Map<String, String> variant3Attributes = new LinkedHashMap<>();
        variant3Attributes.put("RAM Size", "32GB");
        variant3Attributes.put("Storage Capacity", "128GB");
        variant3Attributes.put("Color", "White");
        variant3.setVariantAttributes(variant3Attributes);
        product.getVariants().add(variant3);

        productRepository.save(product);

        sellerRepository.deleteAll();

        Seller seller = new Seller();
        seller.setId(GlobalData.SELLER_ID);
        seller.setSellerId(GlobalData.SELLER_ID);
        seller.setEmail(GlobalData.SELLER_EMAIL);
        seller.setFullName("Academy");
        seller.setBrandName("Academy");
        seller.setCompanyName("Academy");
        seller.setPhoneNumber("+919999999999");
        Address address = new Address();
        address.setAddressLine1("Galaxy Street");
        address.setCity("Star City");
        address.setState("Milky Way");
        address.setCountry("India");
        address.setZip("523372");
        seller.setAddress(address);
        sellerRepository.save(seller);

        Seller anotherSeller = new Seller();
        anotherSeller.setId(GlobalData.SELLER2_ID);
        anotherSeller.setSellerId(GlobalData.SELLER2_ID);
        anotherSeller.setEmail(GlobalData.SELLER2_EMAIL);
        anotherSeller.setFullName("Academy");
        anotherSeller.setBrandName("Academy");
        anotherSeller.setCompanyName("Academy");
        anotherSeller.setPhoneNumber("+919999999999");
        Address anotherSellerAddress = new Address();
        anotherSellerAddress.setAddressLine1("Galaxy Street");
        anotherSellerAddress.setCity("Star City");
        anotherSellerAddress.setState("Milky Way");
        anotherSellerAddress.setCountry("India");
        anotherSellerAddress.setZip("523635");
        anotherSeller.setAddress(address);
        sellerRepository.save(anotherSeller);

        inventoryRepository.deleteAll();

        InventoryUnit variant1Seller1 = new InventoryUnit();
        variant1Seller1.setProductId(GlobalData.PRODUCT_ID);
        variant1Seller1.setSellerId(GlobalData.SELLER_ID);
        variant1Seller1.setVariantId(GlobalData.VARIANT1_ID);
        variant1Seller1.setQuantity(10L);
        variant1Seller1.setUnitPrice(BigDecimal.valueOf(100000));
        variant1Seller1.setInventoryId("inv-1");

        InventoryUnit variant1Seller2 = new InventoryUnit();
        variant1Seller2.setProductId(GlobalData.PRODUCT_ID);
        variant1Seller2.setSellerId(GlobalData.SELLER2_ID);
        variant1Seller2.setVariantId(GlobalData.VARIANT1_ID);
        variant1Seller2.setQuantity(10L);
        variant1Seller2.setUnitPrice(BigDecimal.valueOf(100020));
        variant1Seller2.setInventoryId("inv-2");

        InventoryUnit variant2Seller1 = new InventoryUnit();
        variant2Seller1.setProductId(GlobalData.PRODUCT_ID);
        variant2Seller1.setSellerId(GlobalData.SELLER_ID);
        variant2Seller1.setVariantId(GlobalData.VARIANT2_ID);
        variant2Seller1.setQuantity(0L);
        variant2Seller1.setUnitPrice(BigDecimal.valueOf(100000));
        variant2Seller1.setInventoryId("inv-3");

        InventoryUnit variant2Seller2 = new InventoryUnit();
        variant2Seller2.setProductId(GlobalData.PRODUCT_ID);
        variant2Seller2.setSellerId(GlobalData.SELLER2_ID);
        variant2Seller2.setVariantId(GlobalData.VARIANT2_ID);
        variant2Seller2.setQuantity(10L);
        variant2Seller2.setUnitPrice(BigDecimal.valueOf(100020));
        variant2Seller2.setInventoryId("inv-4");

        InventoryUnit variant3Seller1 = new InventoryUnit();
        variant3Seller1.setProductId(GlobalData.PRODUCT_ID);
        variant3Seller1.setSellerId(GlobalData.SELLER_ID);
        variant3Seller1.setVariantId(GlobalData.VARIANT3_ID);
        variant3Seller1.setQuantity(10L);
        variant3Seller1.setUnitPrice(BigDecimal.valueOf(100000));
        variant3Seller1.setInventoryId("inv-5");

        InventoryUnit variant3Seller2 = new InventoryUnit();
        variant3Seller2.setProductId(GlobalData.PRODUCT_ID);
        variant3Seller2.setSellerId(GlobalData.SELLER2_ID);
        variant3Seller2.setVariantId(GlobalData.VARIANT3_ID);
        variant3Seller2.setQuantity(10L);
        variant3Seller2.setUnitPrice(BigDecimal.valueOf(100020));
        variant3Seller2.setInventoryId("inv-6");

        inventoryRepository.save(variant1Seller1);
        inventoryRepository.save(variant1Seller2);
        inventoryRepository.save(variant2Seller1);
        inventoryRepository.save(variant2Seller2);
        inventoryRepository.save(variant3Seller1);
        inventoryRepository.save(variant3Seller2);


        alreadySetup = true;
    }
}
