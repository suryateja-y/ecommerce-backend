package com.academy.projects.ecommerce.cartmanagementservice.services;

import com.academy.projects.ecommerce.cartmanagementservice.models.Cart;
import com.academy.projects.ecommerce.cartmanagementservice.models.CartUnit;

import java.util.Set;

public interface ICartService {
    Cart add(String userId, Set<CartUnit> cartUnits);
    Cart get(String userId);
    Cart addToCart(String userId, CartUnit cartUnit);
    void clear(String customerId);
}
