package com.academy.projects.ecommerce.ordermanagementservice.clients.services;

import com.academy.projects.ecommerce.ordermanagementservice.clients.dtos.Cart;
import com.academy.projects.ecommerce.ordermanagementservice.clients.dtos.CartUnit;
import com.academy.projects.ecommerce.ordermanagementservice.clients.exceptions.ProductNotInStockException;
import com.academy.projects.ecommerce.ordermanagementservice.models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class CartToOrderService implements ICartToOrderService {
    private final CartServiceClient cartServiceClient;

    @Autowired
    public CartToOrderService(final CartServiceClient cartServiceClient) {
        this.cartServiceClient = cartServiceClient;
    }

    @Override
    public Set<OrderItem> getOrderItems() {
        ResponseEntity<Cart> cartResponse = cartServiceClient.getCart();
        if (cartResponse.getStatusCode().is2xxSuccessful() && cartResponse.getBody() != null) {
            Cart cart = cartResponse.getBody();
            Set<CartUnit> cartUnits = cart.getCartUnits();
            if((cartUnits == null) || cartUnits.isEmpty()) throw new RuntimeException("Cart is empty!!!");
            Set<OrderItem> orderItems = new LinkedHashSet<>();
            for (CartUnit cartUnit : cartUnits)
                orderItems.add(from(cartUnit));
            return orderItems;
        } else throw new RuntimeException("Failed to get the cart items of the user");
    }

    private OrderItem from(CartUnit cartUnit) {
        if(cartUnit.isInStock()) throw new ProductNotInStockException(cartUnit.getVariantId(), cartUnit.getSellerId());
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(cartUnit.getProductId());
        orderItem.setProductName(cartUnit.getProductName());
        orderItem.setVariantId(cartUnit.getVariantId());
        orderItem.setSellerId(cartUnit.getSellerId());
        orderItem.setQuantity(cartUnit.getQuantity());
        orderItem.setUnitPrice(cartUnit.getUnitPrice());
        orderItem.setTotalPrice(cartUnit.getUnitPrice().multiply(BigDecimal.valueOf(cartUnit.getQuantity())));
        return orderItem;
    }
}
