package com.academy.projects.ecommerce.cartmanagementservice.services;

import com.academy.projects.ecommerce.cartmanagementservice.clients.dtos.BulkInventoryDetailsRequestDto;
import com.academy.projects.ecommerce.cartmanagementservice.clients.dtos.InventoryDetailsRequestDto;
import com.academy.projects.ecommerce.cartmanagementservice.clients.services.InventoryManagementServiceClient;
import com.academy.projects.ecommerce.cartmanagementservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.cartmanagementservice.dtos.InventoryDetails;
import com.academy.projects.ecommerce.cartmanagementservice.models.Cart;
import com.academy.projects.ecommerce.cartmanagementservice.models.CartUnit;
import com.academy.projects.ecommerce.cartmanagementservice.repositories.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final InventoryManagementServiceClient inventoryManagementServiceClient;
    private final IdGenerator idGenerator;

    private final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    public CartService(final CartRepository cartRepository, final InventoryManagementServiceClient inventoryManagementServiceClient, final IdGenerator idGenerator) {
        this.cartRepository = cartRepository;
        this.inventoryManagementServiceClient = inventoryManagementServiceClient;
        this.idGenerator = idGenerator;
    }

    @Override
    public Cart add(String userId, Set<CartUnit> cartUnits) {
        cartUnits = (cartUnits == null) ? new LinkedHashSet<>() : cartUnits;
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if(cart == null) {
            cart = new Cart();
            cart.setId(idGenerator.getId(Cart.CART_SEQUENCE));
        }
        cart.setUserId(userId);
        cartUnits = validateAndUpdate(cartUnits);
        cart.setCartUnits(cartUnits);
        cart.setSubTotal(calculateSubTotal(cartUnits));

        return cartRepository.save(cart);
    }

    @Override
    @Cacheable(value = "carts", key = "#userId")
    public Cart get(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
        cart.setCartUnits(validateAndUpdate(cart.getCartUnits()));
        cart.setSubTotal(calculateSubTotal(cart.getCartUnits()));
        if(cart.getId() == null)
            cart.setId(idGenerator.getId(Cart.CART_SEQUENCE));
        return cartRepository.save(cart);
    }

    @Override
    public Cart addToCart(String userId, CartUnit cartUnit) {
        if(cartUnit == null) throw new IllegalArgumentException("CartUnit cannot be null!!!");
        return (cartUnit.getQuantity() <= 0) ? deleteFromCart(userId, cartUnit) : updateCartUnit(userId, cartUnit);
    }

    @Override
    public void clear(String customerId) {
        cartRepository.findByUserId(customerId).ifPresent(cartRepository::delete);
    }

    private Cart updateCartUnit(String userId, CartUnit cartUnit) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
        if(cart.getId() == null)
            cart.setId(idGenerator.getId(Cart.CART_SEQUENCE));
        cart.getCartUnits().remove(cartUnit);
        cart.getCartUnits().add(cartUnit);
        return cartRepository.save(cart);
    }

    private Cart deleteFromCart(String userId, CartUnit cartUnit) {
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
        if(cart.getId() == null) return cart;
        cart.getCartUnits().remove(cartUnit);
        return cartRepository.save(cart);
    }

    private BigDecimal calculateSubTotal(Set<CartUnit> cartUnits) {
        return cartUnits.stream().filter(CartUnit::isInStock).map(CartUnit::getUnitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<CartUnit> validateAndUpdate(Set<CartUnit> cartUnits) {
        List<CartUnit> cartUnitList = new LinkedList<>(cartUnits);
        List<InventoryDetails> inventoryDetails = getDetails(cartUnitList);
        for(int cartUnit = 0; cartUnit < cartUnits.size(); cartUnit++) {
            InventoryDetails inventoryDetail = inventoryDetails.get(cartUnit);
            cartUnitList.get(cartUnit).setUnitPrice(inventoryDetail.getUnitPrice());
            cartUnitList.get(cartUnit).setInStock(inventoryDetail.isInStock());
        }
        return new LinkedHashSet<>(cartUnitList);
    }

    private List<InventoryDetails> getDetails(List<CartUnit> cartUnits) {
        try {
            List<InventoryDetailsRequestDto> requestDtoList = cartUnits.stream().map(this::from).toList();
            ResponseEntity<List<InventoryDetails>> inventoryDetailsList = inventoryManagementServiceClient.getDetails(BulkInventoryDetailsRequestDto.builder().requests(requestDtoList).build());
            if(inventoryDetailsList.getStatusCode().is2xxSuccessful()) return inventoryDetailsList.getBody();
            throw new RuntimeException(inventoryDetailsList.getStatusCode() + " " + inventoryDetailsList.getBody());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private InventoryDetailsRequestDto from(CartUnit cartUnit) {
        InventoryDetailsRequestDto dto = new InventoryDetailsRequestDto();
        dto.setSellerId(cartUnit.getSellerId());
        dto.setVariantId(cartUnit.getVariantId());
        return dto;
    }
}
