package com.academy.projects.ecommerce.usermanagementservice.services.usermanagement;

import com.academy.projects.ecommerce.usermanagementservice.dtos.SellerRegistrationDto;
import com.academy.projects.ecommerce.usermanagementservice.models.Address;
import com.academy.projects.ecommerce.usermanagementservice.models.Seller;
import com.academy.projects.ecommerce.usermanagementservice.models.UserState;

import java.util.List;

public interface ISellerManagementService {
    Seller register(SellerRegistrationDto sellerRegistrationDto);
    Seller update(Seller seller);
    Seller getSeller(String id);
    List<Seller> getSellers(int page, int pageSize, UserState userState);
    Seller updateState(String userId, UserState userState, String updaterId);
    Seller addAddress(String name, Address address);
    Seller updateAddress(String name, Address address);
    Address getAddress(String name);
}
