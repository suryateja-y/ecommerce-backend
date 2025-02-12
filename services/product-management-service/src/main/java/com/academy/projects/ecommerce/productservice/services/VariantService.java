package com.academy.projects.ecommerce.productservice.services;

import com.academy.projects.ecommerce.productservice.configurations.Patcher;
import com.academy.projects.ecommerce.productservice.exceptions.AttributeNotProvidedException;
import com.academy.projects.ecommerce.productservice.exceptions.IdNotProvidedException;
import com.academy.projects.ecommerce.productservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.productservice.kafka.dtos.VariantDto;
import com.academy.projects.ecommerce.productservice.kafka.producer.variant.VariantUpdateManager;
import com.academy.projects.ecommerce.productservice.models.Attribute;
import com.academy.projects.ecommerce.productservice.models.Variant;
import com.academy.projects.ecommerce.productservice.repositories.VariantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VariantService implements IVariantService {
    private final VariantRepository variantRepository;
    private final Patcher patcher;
    private final VariantUpdateManager variantUpdateManager;
    private final IProductService productService;

    private final Logger logger = LoggerFactory.getLogger(VariantService.class);

    @Autowired
    public VariantService(VariantRepository variantRepository, Patcher patcher, VariantUpdateManager variantUpdateManager, IProductService productService) {
        this.variantRepository = variantRepository;
        this.patcher = patcher;
        this.variantUpdateManager = variantUpdateManager;
        this.productService = productService;
    }

    @Override
    public void consumeVariant(Variant variant) {
        if (variant.getId() == null) throw new IdNotProvidedException("Variant id not provided!!!");
        Variant savedVariant = variantRepository.findById(variant.getId()).orElse(null);
        if(savedVariant == null) {
            savedVariant = variantRepository.save(variant);
            productService.addVariant(variant);
        }  else {
            updateVariant(savedVariant, variant);
            savedVariant.setId(variant.getId());
            savedVariant = variantRepository.save(savedVariant);
        }

        variantUpdateManager.notifyObservers(new VariantDto(savedVariant, Action.CREATE));
        logger.info("Variant: {} has been created successfully!!!", savedVariant);
    }

    private void updateVariant(Variant savedVariant, Variant variant) {
        patcher.entity(savedVariant, variant, Variant.class);
        List<Attribute> expectedAttributes = savedVariant.getProduct().getCategory().getVariantAttributes();
        List<Attribute> attributes = this.analyseAttributes(expectedAttributes, variant.getAttributes());
        savedVariant.setAttributes(attributes);
    }

    private List<Attribute> analyseAttributes(List<Attribute> expectedAttributes, List<Attribute> actualAttributes) {
        List<Attribute> attributes = new ArrayList<>();
        for(Attribute expectedAttribute : expectedAttributes) {
            boolean found = false;
            for(Attribute actualAttribute : actualAttributes) {
                if(expectedAttribute.getName().equals(actualAttribute.getName())) {
                    Attribute newAttribute = new Attribute();
                    newAttribute.setName(expectedAttribute.getName());
                    newAttribute.setValue(actualAttribute.getValue());
                    newAttribute.setRequired(expectedAttribute.isRequired());
                    newAttribute.setDataType(expectedAttribute.getDataType());
                    attributes.add(newAttribute);
                    found = true;
                }
            }
            if(!found) throw new AttributeNotProvidedException(expectedAttribute);
        }
        return attributes;
    }

    @Override
    public void deleteVariant(Variant variant) {
        variantRepository.findById(variant.getId()).ifPresent(variantRepository::delete);
        variantUpdateManager.notifyObservers(new VariantDto(variant, Action.DELETE));
        logger.info("Variant: {} has been deleted successfully!!!", variant);
        // Send notification to the Seller and Product Manager
    }
}
