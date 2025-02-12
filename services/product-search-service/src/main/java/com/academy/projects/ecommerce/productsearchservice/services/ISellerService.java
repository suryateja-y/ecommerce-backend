package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.SellerDto;
import com.academy.projects.ecommerce.productsearchservice.models.Seller;

public interface ISellerService {
    Seller getBySellerId(String sellerId);
    void update(SellerDto sellerDto);
}
