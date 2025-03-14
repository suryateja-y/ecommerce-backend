package com.academy.projects.ecommerce.ordermanagementservice.starters;

import com.academy.projects.ecommerce.ordermanagementservice.models.InventoryUnit;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class InitializeData implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    private final InventoryRepository inventoryRepository;
    private final DeliveryFeasibilityRepository deliveryFeasibilityRepository;
    private final InvoiceRepository invoiceRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final PreOrderRepository preOrderRepository;

    @Autowired
    public InitializeData(InventoryRepository inventoryRepository, DeliveryFeasibilityRepository deliveryFeasibilityRepository, InvoiceRepository invoiceRepository, OrderItemRepository orderItemRepository, OrderRepository orderRepository, PaymentDetailsRepository paymentDetailsRepository, PreOrderRepository preOrderRepository) {
        this.inventoryRepository = inventoryRepository;
        this.deliveryFeasibilityRepository = deliveryFeasibilityRepository;
        this.invoiceRepository = invoiceRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.preOrderRepository = preOrderRepository;
    }
    @SuppressWarnings("NullableProblems")
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;
        inventoryRepository.deleteAll();
        deliveryFeasibilityRepository.deleteAll();
        invoiceRepository.deleteAll();
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        paymentDetailsRepository.deleteAll();
        preOrderRepository.deleteAll();

        InventoryUnit variant1Seller1 = new InventoryUnit();
        variant1Seller1.setSellerId(GlobalData.SELLER_ID);
        variant1Seller1.setVariantId(GlobalData.VARIANT1_ID);
        variant1Seller1.setQuantity(10L);
        variant1Seller1.setUnitPrice(BigDecimal.valueOf(100000));

        InventoryUnit variant1Seller2 = new InventoryUnit();
        variant1Seller2.setSellerId(GlobalData.SELLER2_ID);
        variant1Seller2.setVariantId(GlobalData.VARIANT1_ID);
        variant1Seller2.setQuantity(10L);
        variant1Seller2.setUnitPrice(BigDecimal.valueOf(100020));

        InventoryUnit variant2Seller1 = new InventoryUnit();
        variant2Seller1.setSellerId(GlobalData.SELLER_ID);
        variant2Seller1.setVariantId(GlobalData.VARIANT2_ID);
        variant2Seller1.setQuantity(10L);
        variant2Seller1.setUnitPrice(BigDecimal.valueOf(100000));

        InventoryUnit variant2Seller2 = new InventoryUnit();
        variant2Seller2.setSellerId(GlobalData.SELLER2_ID);
        variant2Seller2.setVariantId(GlobalData.VARIANT2_ID);
        variant2Seller2.setQuantity(10L);
        variant2Seller2.setUnitPrice(BigDecimal.valueOf(100020));

        InventoryUnit variant3Seller1 = new InventoryUnit();
        variant3Seller1.setSellerId(GlobalData.SELLER_ID);
        variant3Seller1.setVariantId(GlobalData.VARIANT3_ID);
        variant3Seller1.setQuantity(10L);
        variant3Seller1.setUnitPrice(BigDecimal.valueOf(100000));

        InventoryUnit variant3Seller2 = new InventoryUnit();
        variant3Seller2.setSellerId(GlobalData.SELLER2_ID);
        variant3Seller2.setVariantId(GlobalData.VARIANT3_ID);
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
