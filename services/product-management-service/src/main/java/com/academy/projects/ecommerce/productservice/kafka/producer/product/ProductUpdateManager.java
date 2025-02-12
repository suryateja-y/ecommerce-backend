package com.academy.projects.ecommerce.productservice.kafka.producer.product;

import com.academy.projects.ecommerce.productservice.kafka.dtos.ProductDto;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ProductUpdateManager {
    private final List<ProductObserver> observers = new LinkedList<>();

    public void addObserver(ProductObserver observer) {
        observers.add(observer);
    }
    public void notifyObservers(ProductDto productDto) {
        for (ProductObserver observer : observers) {
            observer.sendUpdate(productDto);
        }
    }

}
