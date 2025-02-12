package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.clients.services.UserManagementServiceClient;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.DetailsRequestDto;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.DetailsResponseDto;
import com.academy.projects.ecommerce.ordermanagementservice.dtos.SellerOption;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.AddressNotProvidedException;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.DeliveryNotPossibleException;
import com.academy.projects.ecommerce.ordermanagementservice.exceptions.SellerNotFoundException;
import com.academy.projects.ecommerce.ordermanagementservice.models.Address;
import com.academy.projects.ecommerce.ordermanagementservice.models.InventoryUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class DetailsService implements IDetailsService {

    private final UserManagementServiceClient userManagementServiceClient;
    private final IInventoryService inventoryService;

    private final Logger logger = LoggerFactory.getLogger(DetailsService.class);

    @Autowired
    public DetailsService(UserManagementServiceClient userManagementServiceClient, IInventoryService inventoryService) {
        this.userManagementServiceClient = userManagementServiceClient;
        this.inventoryService = inventoryService;
    }
    @Override
    public DetailsResponseDto getDetails(DetailsRequestDto requestDto) {
        DetailsResponseDto responseDto = new DetailsResponseDto();
        InventoryUnit inventoryUnit = inventoryService.getInventoryByVariantIdAndSellerId(requestDto.getVariantId(), requestDto.getSellerId());
        if (inventoryUnit.getQuantity() <= 0) {
            responseDto.setInStock(false);
        } else {
            responseDto.setInStock(true);
            responseDto.setUnitPrice(inventoryUnit.getUnitPrice());
            Address sellerAddress = getSellerAddress(inventoryUnit.getSellerId());
            responseDto.setEta(calculateEta(requestDto.getUserAddress(), sellerAddress));
        }
        return responseDto;
    }

    @Override
    public List<SellerOption> getSellerOptions(String variantId, Address userAddress) {
        List<SellerOption> sellerOptions = new LinkedList<>();
        List<InventoryUnit> inventoryUnits = inventoryService.getAllByVariant(variantId);

        for(InventoryUnit inventoryUnit : inventoryUnits) {
            if(inventoryUnit.getQuantity() <= 0) continue;
            Address sellerAddress = getSellerAddress(inventoryUnit.getSellerId());
            SellerOption sellerOption = new SellerOption();
            sellerOption.setSellerId(inventoryUnit.getSellerId());
            sellerOption.setEta(calculateEta(userAddress, sellerAddress));
            sellerOption.setPrice(inventoryUnit.getUnitPrice());
            sellerOptions.add(sellerOption);
        }
        return sellerOptions;
    }

    private Address getSellerAddress(String sellerId) {
        try {
            return userManagementServiceClient.getAddress(sellerId);
        } catch (Exception e) {
            throw new SellerNotFoundException(sellerId, e.getMessage());
        }
    }

    private void validateUserAddress(Address address, String addressType) {
        if(address == null) throw new AddressNotProvidedException(addressType + "Address: Address not provided for the search!!!");
        if(address.getCountry() == null) throw new AddressNotProvidedException(addressType + "Address: Country not provided for the search!!!");
        if(address.getZip() == null) throw new AddressNotProvidedException(addressType + "Address: Zip code not provided for the search!!!");
    }

    private Date calculateEta(Address userAddress, Address sellerAddress) {
        validateUserAddress(userAddress, "User");
        validateUserAddress(sellerAddress, "Seller");
        if(!userAddress.getCountry().equalsIgnoreCase(sellerAddress.getCountry())) throw new DeliveryNotPossibleException(userAddress.getCountry(), sellerAddress.getCountry());
        long etaInHours = calculateEta(userAddress.getZip(), sellerAddress.getZip());
        return from(etaInHours);
    }

    private static Date from(long etaInHours) {
        Date current = new Date();
        current.setTime(current.getTime() + (etaInHours * 60 * 60 * 1000));
        return current;
    }

    private long calculateEta(String userZipCode, String sellerZipCode) {
        try {
            int[] hours = { 3, 6, 12, 18, 24, 48 };
            long userZip = Long.parseLong(userZipCode);
            long sellerZip = Long.parseLong(sellerZipCode);
            long difference = Math.abs(userZip - sellerZip);
            int index = 0;
            long calculatedEta = 0;
            while(difference > 0) {
                long digit = difference % 10;
                calculatedEta += (hours[index] * digit);
                difference = difference / 10;
                index++;
            }
            if(calculatedEta < 24)
                calculatedEta = 24;
            return calculatedEta;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }
    }
}
