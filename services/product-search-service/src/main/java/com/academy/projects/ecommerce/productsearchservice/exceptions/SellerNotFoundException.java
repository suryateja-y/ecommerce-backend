package com.academy.projects.ecommerce.productsearchservice.exceptions;

import lombok.ToString;

@ToString
public class SellerNotFoundException extends RuntimeException {
    public SellerNotFoundException(String sellerId) {
        super("Seller with id '" + sellerId + "' not found!!!");
    }
}
