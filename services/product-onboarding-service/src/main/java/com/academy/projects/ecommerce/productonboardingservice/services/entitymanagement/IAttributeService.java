package com.academy.projects.ecommerce.productonboardingservice.services.entitymanagement;

import com.academy.projects.ecommerce.productonboardingservice.models.Attribute;

import java.util.List;

public interface IAttributeService {
    List<Attribute> merge(List<Attribute> currentAttributes, List<Attribute> newAttributes);
}
