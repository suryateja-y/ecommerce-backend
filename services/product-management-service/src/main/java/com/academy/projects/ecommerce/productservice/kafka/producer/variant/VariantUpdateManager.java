package com.academy.projects.ecommerce.productservice.kafka.producer.variant;

import com.academy.projects.ecommerce.productservice.kafka.dtos.VariantDto;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class VariantUpdateManager {
    private final List<VariantObserver> observers = new LinkedList<>();
    public void addObserver(VariantObserver observer) {
        observers.add(observer);
    }
    public void notifyObservers(VariantDto variantDto) {
        for (VariantObserver observer : observers) {
            observer.sendUpdate(variantDto);
        }
    }

}
