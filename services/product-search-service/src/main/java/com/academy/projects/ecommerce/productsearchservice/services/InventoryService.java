package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.InventoryUnitContainer;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.InventoryUnitDto;
import com.academy.projects.ecommerce.productsearchservice.models.InventoryUnit;
import com.academy.projects.ecommerce.productsearchservice.models.Product;
import com.academy.projects.ecommerce.productsearchservice.models.Seller;
import com.academy.projects.ecommerce.productsearchservice.models.Variant;
import com.academy.projects.ecommerce.productsearchservice.repositories.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService implements IInventoryService {

    private final IVariantService variantService;
    private final ISellerService sellerService;
    private final IProductService productService;
    private final InventoryRepository inventoryRepository;
    private final IdGenerator idGenerator;

    private final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    public InventoryService(IVariantService variantService, SellerService sellerService, ProductService productService, InventoryRepository inventoryRepository, IdGenerator idGenerator) {
        this.variantService = variantService;
        this.sellerService = sellerService;
        this.productService = productService;
        this.inventoryRepository = inventoryRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public InventoryUnit add(InventoryUnitDto inventoryUnitDto) {
        if(inventoryUnitDto.getAction().equals(Action.DELETE)) {
            this.deleteInventory(inventoryUnitDto.getInventoryUnit().getId());
            return null;
        } else {
            InventoryUnitContainer inventoryUnitContainer = new InventoryUnitContainer();
            Seller seller = sellerService.getBySellerId(inventoryUnitContainer.getSellerId());
            Product product = productService.getByProductId(inventoryUnitContainer.getProductId());
            Variant variant = variantService.getByVariantId(product, inventoryUnitContainer.getVariantId());

            product = productService.update(product, variant, seller, inventoryUnitContainer.getUnitPrice());
            logger.info("Product saved with the id: '{}'", product.getId());

            InventoryUnit inventoryUnit = inventoryRepository.findByVariantIdAndSellerId(inventoryUnitContainer.getVariantId(), inventoryUnitContainer.getSellerId()).orElse(null);
            if (inventoryUnit != null) {
                inventoryUnit.setUnitPrice(inventoryUnitContainer.getUnitPrice());
                inventoryUnit.setQuantity(inventoryUnitContainer.getQuantity());
            } else {
                inventoryUnit = get(inventoryUnitContainer);
                inventoryUnit.setId(idGenerator.getId(InventoryUnit.SEQUENCE_NAME));
            }
            return inventoryRepository.save(inventoryUnit);
        }
    }

    @Override
    public List<InventoryUnit> getAll(String productId, String variantId) {
        return inventoryRepository.findAllByProductIdAndVariantId(productId, variantId);
    }

    private InventoryUnit get(InventoryUnitContainer inventoryUnitContainer) {
        InventoryUnit inventoryUnit = new InventoryUnit();
        inventoryUnit.setInventoryId(inventoryUnitContainer.getId());
        inventoryUnit.setVariantId(inventoryUnitContainer.getVariantId());
        inventoryUnit.setSellerId(inventoryUnitContainer.getSellerId());
        inventoryUnit.setUnitPrice(inventoryUnitContainer.getUnitPrice());
        inventoryUnit.setQuantity(inventoryUnitContainer.getQuantity());
        inventoryUnit.setProductId(inventoryUnitContainer.getProductId());
        return inventoryUnit;
    }

    private void deleteInventory(String inventoryId) {
        inventoryRepository.findByInventoryId(inventoryId).ifPresent(inventoryRepository::delete);
    }
}
