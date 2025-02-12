package com.academy.projects.ecommerce.productservice.services;

import com.academy.projects.ecommerce.productservice.clients.dtos.DetailsRequestDto;
import com.academy.projects.ecommerce.productservice.clients.dtos.DetailsResponseDto;
import com.academy.projects.ecommerce.productservice.clients.services.InventoryManagementServiceClient;
import com.academy.projects.ecommerce.productservice.configurations.Patcher;
import com.academy.projects.ecommerce.productservice.dtos.*;
import com.academy.projects.ecommerce.productservice.exceptions.*;
import com.academy.projects.ecommerce.productservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.productservice.kafka.dtos.ProductDto;
import com.academy.projects.ecommerce.productservice.kafka.producer.product.ProductUpdateManager;
import com.academy.projects.ecommerce.productservice.models.Attribute;
import com.academy.projects.ecommerce.productservice.models.Category;
import com.academy.projects.ecommerce.productservice.models.Product;
import com.academy.projects.ecommerce.productservice.models.Variant;
import com.academy.projects.ecommerce.productservice.repositories.CategoryRepository;
import com.academy.projects.ecommerce.productservice.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Patcher patcher;
    private final ProductUpdateManager productUpdateManager;
    private final InventoryManagementServiceClient inventoryManagementServiceClient;

    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, Patcher patcher, ProductUpdateManager productUpdateManager, InventoryManagementServiceClient inventoryManagementServiceClient) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.patcher = patcher;
        this.productUpdateManager = productUpdateManager;
        this.inventoryManagementServiceClient = inventoryManagementServiceClient;
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.findById(product.getId()).ifPresent(productRepository::delete);
        productUpdateManager.notifyObservers(new ProductDto(product, Action.DELETE));
        logger.info("Deleted product: '{}'!!!", product);

        // Send notification to Product Manager and All Sellers with Inventory
    }

    @Override
    public void consumeProduct(Product product) {
        if (product.getId() == null) throw new IdNotProvidedException("Product id not provided!!!");
        Product foundProduct = productRepository.findById(product.getId()).orElse(null);
        if(foundProduct == null) foundProduct = productRepository.save(product);
        else {
            updateProduct(foundProduct, product);
            foundProduct.setId(product.getId());
            foundProduct = productRepository.save(foundProduct);
        }

        productUpdateManager.notifyObservers(new ProductDto(foundProduct, Action.CREATE));

        // Send Notification to the Product Manager and Seller
    }

    @Override
    public Product addVariant(Variant variant) {
        if(variant.getProduct() == null) throw new ProductNotProvidedException("Product not provided in the variant!!!");
        Product product = productRepository.findById(variant.getProduct().getId()).orElseThrow(() -> new ProductNotFoundException(variant.getProduct().getId()));
        product.getVariants().add(variant);
        product = productRepository.save(product);

        productUpdateManager.notifyObservers(new ProductDto(product, Action.UPDATE));
        // Send Notification to the Product Manager and Seller
        return product;
    }

    @Override
    public DetailedProductDto get(GetProductRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.getProductId()).orElseThrow(() -> new ProductNotFoundException(requestDto.getProductId()));
        Variant variant = getVariant(product, requestDto.getVariantId());
        DeliveryDetails deliveryDetails = getDeliveryDetails(requestDto);
        DetailedProductDto productDto = from(product);
        productDto.setVariants(getVariants(product, variant, deliveryDetails));
        return productDto;
    }

    private DetailedProductDto from(Product product) {
        DetailedProductDto productDto = new DetailedProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCompany(product.getCompany());
        productDto.setCategory(from(product.getCategory()));
        productDto.setAttributes(product.getAttributes());
        productDto.setImage(product.getImage());
        productDto.setBrandName(product.getBrandName());
        productDto.setDimensionalMetrics(product.getDimensionalMetrics());
        return productDto;
    }

    private CategoryWrapper from(Category category) {
        CategoryWrapper categoryWrapper = new CategoryWrapper();
        categoryWrapper.setCategoryName(category.getCategoryName());
        categoryWrapper.setId(category.getId());
        categoryWrapper.setCategoryDescription(category.getCategoryDescription());
        categoryWrapper.setHighLevelCategory(category.getHighLevelCategory());
        return categoryWrapper;
    }

    private Variant getVariant(Product product, String variantId) {
        if(product.getVariants() == null) throw new VariantNotFoundException(product.getId(), variantId);
        for(Variant variant : product.getVariants()) {
            if(variant.getId().equals(variantId)) return variant;
        }
        throw new VariantNotFoundException(product.getId(), variantId);
    }

    private List<VariantDto> getVariants(Product product, Variant variant, DeliveryDetails deliveryDetails) {
        List<VariantDto> variants = new LinkedList<>();
        for(Variant productVariant : product.getVariants() ) {
            VariantDto variantDto = new VariantDto();
            variantDto.setVariant(from(productVariant));
            if(productVariant.getId().equals(variant.getId()))
                variantDto.setDeliveryDetails(deliveryDetails);
            else
                variantDto.setDeliveryDetails(null);
            variants.add(variantDto);
        }
        return variants;
    }

    private VariantWrapper from(Variant variant) {
        VariantWrapper variantWrapper = new VariantWrapper();
        variantWrapper.setAttributes(variant.getAttributes());
        return variantWrapper;
    }


    private DeliveryDetails getDeliveryDetails(GetProductRequestDto requestDto) {
        try {
            DetailsRequestDto detailsRequestDto = new DetailsRequestDto();
            detailsRequestDto.setProductId(requestDto.getProductId());
            detailsRequestDto.setVariantId(requestDto.getVariantId());
            detailsRequestDto.setSellerId(requestDto.getSellerId());
            detailsRequestDto.setUserAddress(requestDto.getUserAddress());
            ResponseEntity<DetailsResponseDto> response = inventoryManagementServiceClient.calculate(detailsRequestDto);
            if(response.getStatusCode().is2xxSuccessful()) {
                return from(Objects.requireNonNull(response.getBody()));
            } else return new DeliveryDetails(false, BigDecimal.ZERO, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve the delivery details >>> " + e.getMessage());
        }
    }

    private DeliveryDetails from(DetailsResponseDto responseDto) {
        DeliveryDetails deliveryDetails = new DeliveryDetails();
        deliveryDetails.setInStock(responseDto.isInStock());
        deliveryDetails.setUnitPrice(responseDto.getUnitPrice());
        deliveryDetails.setEta(responseDto.getEta());
        return deliveryDetails;
    }

    private void updateProduct(Product savedProduct, Product product) {
        patcher.entity(savedProduct, product, Product.class);

        // Updating the Category
        if(product.getCategory() != null) {
            addCategoryToProduct(product);
        }

        // Updating attributes
        if(product.getAttributes() != null) this.updateAttributes(product, savedProduct, savedProduct.getCategory().getAttributes());
    }

    private void addCategoryToProduct(Product product) {
        Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new CategoryNotFoundException(product.getCategory().getId()));
        List<Attribute> requiredAttributes = category.getAttributes();
        List<Attribute> actualAttributes = product.getAttributes();
        List<Attribute> attributesToAdd = this.validateAttributes(requiredAttributes, actualAttributes);
        product.setAttributes(attributesToAdd);
    }

    private List<Attribute> validateAttributes(List<Attribute> requiredAttributes, List<Attribute> actualAttributes) {
        if((actualAttributes == null) || actualAttributes.isEmpty()) return List.of();
        List<Attribute> attributes = new ArrayList<>();
        for(Attribute requiredAttribute : requiredAttributes) {
            boolean found = false;
            for(Attribute actualAttribute : actualAttributes) {
                if(requiredAttribute.getName().equals(actualAttribute.getName())) {
                    attributes.add(requiredAttribute);
                    found = true;
                }
            }
            if(!found) throw new AttributeNotFoundException(requiredAttribute.getName());
        }
        return attributes;
    }

    private void updateAttributes(Product product, Product productToUpdate, List<Attribute> allowedAttributes) {
        for(Attribute requiredAttribute : product.getAttributes()) {
            if(this.isAttributeAlreadyAdded(requiredAttribute, productToUpdate.getAttributes())) continue;
            if(this.isAttributeAllowed(requiredAttribute, allowedAttributes)) productToUpdate.getAttributes().add(requiredAttribute);
        }
    }

    private boolean isAttributeAllowed(Attribute attribute, List<Attribute> allowedAttributes) {
        for(Attribute attributeToCheck : allowedAttributes) {
            if(attribute.getName().equals(attributeToCheck.getName())) return true;
        }
        return false;
    }
    private boolean isAttributeAlreadyAdded(Attribute attribute, List<Attribute> currentAttributes) {
        for(Attribute attributeToCheck : currentAttributes) {
            if(attribute.getName().equals(attributeToCheck.getName())) return true;
        }
        return false;
    }
}
