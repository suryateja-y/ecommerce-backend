package com.academy.projects.ecommerce.inventorymanagementservice.starters;

import com.academy.projects.ecommerce.inventorymanagementservice.models.InventoryUnit;
import com.academy.projects.ecommerce.inventorymanagementservice.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private final InventoryRepository inventoryRepository;
    private boolean alreadySetup = false;

    @Autowired
    public InitializeData(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;

        inventoryRepository.deleteAll();

        InventoryUnit variant1Seller1 = new InventoryUnit();
        variant1Seller1.setSellerId(GlobalData.SELLER_ID);
        variant1Seller1.setVariantId("vari-f8dfbd8e-8390-427f-9c48-ab251b218153");
        variant1Seller1.setQuantity(10L);
        variant1Seller1.setUnitPrice(BigDecimal.valueOf(100000));

        InventoryUnit variant1Seller2 = new InventoryUnit();
        variant1Seller2.setSellerId(GlobalData.SELLER2_ID);
        variant1Seller2.setVariantId("vari-f8dfbd8e-8390-427f-9c48-ab251b218153");
        variant1Seller2.setQuantity(10L);
        variant1Seller2.setUnitPrice(BigDecimal.valueOf(100020));

        InventoryUnit variant2Seller1 = new InventoryUnit();
        variant2Seller1.setSellerId(GlobalData.SELLER_ID);
        variant2Seller1.setVariantId("vari-f8dfbd8e-8390-427f-9c48-ab251b218154");
        variant2Seller1.setQuantity(0L);
        variant2Seller1.setUnitPrice(BigDecimal.valueOf(100000));

        InventoryUnit variant2Seller2 = new InventoryUnit();
        variant2Seller2.setSellerId(GlobalData.SELLER2_ID);
        variant2Seller2.setVariantId("vari-f8dfbd8e-8390-427f-9c48-ab251b218154");
        variant2Seller2.setQuantity(10L);
        variant2Seller2.setUnitPrice(BigDecimal.valueOf(100020));

        InventoryUnit variant3Seller1 = new InventoryUnit();
        variant3Seller1.setSellerId(GlobalData.SELLER_ID);
        variant3Seller1.setVariantId("vari-f8dfbd8e-8390-427f-9c48-ab251b218155");
        variant3Seller1.setQuantity(10L);
        variant3Seller1.setUnitPrice(BigDecimal.valueOf(100000));

        InventoryUnit variant3Seller2 = new InventoryUnit();
        variant3Seller2.setSellerId(GlobalData.SELLER2_ID);
        variant3Seller2.setVariantId("vari-f8dfbd8e-8390-427f-9c48-ab251b218155");
        variant3Seller2.setQuantity(10L);
        variant3Seller2.setUnitPrice(BigDecimal.valueOf(100020));

        inventoryRepository.save(variant1Seller1);
        inventoryRepository.save(variant1Seller2);
        inventoryRepository.save(variant2Seller1);
        inventoryRepository.save(variant2Seller2);
        inventoryRepository.save(variant3Seller1);
        inventoryRepository.save(variant3Seller2);
        alreadySetup = true;
    }
}
