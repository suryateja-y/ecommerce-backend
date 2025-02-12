package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.exceptions.VariantNotFoundException;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.VariantActionDto;
import com.academy.projects.ecommerce.productsearchservice.models.Product;
import com.academy.projects.ecommerce.productsearchservice.models.Variant;
import com.academy.projects.ecommerce.productsearchservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VariantService implements IVariantService {
    private final IProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public VariantService(IProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Override
    public void update(VariantActionDto variantActionDto) {
        if(variantActionDto.getAction().equals(Action.DELETE)) {
            this.deleteVariant(variantActionDto.getVariant().getProductId(), variantActionDto.getVariant().getVariantId());
        } else {
            Variant actionVariant = variantActionDto.getVariant();
            Product product = productService.getByProductId(actionVariant.getProductId());
            Variant variant = this.getOrNull(product, actionVariant.getVariantId());
            if (variant == null) {
//                Variant newVariant = new Variant(actionVariant.getProductId(), actionVariant.getVariantId(), actionVariant.getVariantAttributes());
                Variant newVariant = new Variant();
                newVariant.setProductId(actionVariant.getProductId());
                newVariant.setVariantId(actionVariant.getVariantId());
                newVariant.setVariantAttributes(actionVariant.getVariantAttributes());
                product.getVariants().add(newVariant);
            } else {
                Map<String, String> attributes = variant.getVariantAttributes();
                attributes.putAll(actionVariant.getVariantAttributes());
                variant.setVariantAttributes(attributes);
            }
            productService.update(product);
        }
    }

    @Override
    public Variant getByVariantId(Product product, String variantId) {
        return this.get(product, variantId);
    }

    private Variant get(Product product, String variantId) {
        return product.getVariants().stream().filter(productVariant -> productVariant.getVariantId().equals(variantId)).findFirst().orElseThrow(() -> new VariantNotFoundException(variantId));
    }

    private Variant getOrNull(Product product, String variantId) {
        return product.getVariants().stream().filter(productVariant -> productVariant.getVariantId().equals(variantId)).findFirst().orElse(null);
    }

    private void deleteVariant(String productId, String variantId) {
        Product product = productService.getByProductId(productId);
        Variant variant = this.getOrNull(product, variantId);
        if(variant != null) {
            product.getVariants().remove(variant);
        }
        productRepository.save(product);
    }
}
