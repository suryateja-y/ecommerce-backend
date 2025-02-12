package com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement;

import com.academy.projects.ecommerce.productonboardingservice.models.Attribute;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttributeService implements IAttributeService {
    @Override
    public List<Attribute> merge(List<Attribute> currentAttributes, List<Attribute> newAttributes) {
        List<Attribute> mergedAttributes = new ArrayList<>(currentAttributes);
        for(Attribute newAttribute : newAttributes) {
            Attribute attribute = currentAttributes.stream().filter(attr -> attr.getName().equals(newAttribute.getName())).findFirst().orElse(null);
            if(attribute == null)
                mergedAttributes.add(newAttribute);
        }
        return mergedAttributes;
    }
}
