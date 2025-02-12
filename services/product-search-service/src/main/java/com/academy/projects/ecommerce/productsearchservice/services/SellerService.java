package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.productsearchservice.exceptions.SellerNotFoundException;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.SellerContainer;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.SellerDto;
import com.academy.projects.ecommerce.productsearchservice.models.Seller;
import com.academy.projects.ecommerce.productsearchservice.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService implements ISellerService {

    private final SellerRepository sellerRepository;
    private final IdGenerator idGenerator;

    @Autowired
    public SellerService(SellerRepository sellerRepository, IdGenerator idGenerator) {
        this.sellerRepository = sellerRepository;
        this.idGenerator = idGenerator;
    }
    @Override
    public Seller getBySellerId(String sellerId) {
        return sellerRepository.findBySellerId(sellerId).orElseThrow(() -> new SellerNotFoundException(sellerId));
    }

    @Override
    public void update(SellerDto sellerDto) {
        if(sellerDto.getAction().equals(Action.DELETE)) {
            this.deleteSeller(sellerDto.getSellerContainer().getId());
        } else {
            SellerContainer sellerContainer = sellerDto.getSellerContainer();
            Seller seller = sellerRepository.findBySellerId(sellerContainer.getId()).orElse(null);
            if(seller == null) {
                seller = new Seller();
                seller.setId(idGenerator.getId(Seller.SEQUENCE_NAME));
            }
            get(seller, sellerContainer);
            sellerRepository.save(seller);
        }

    }

    private void get(Seller seller, SellerContainer sellerContainer) {
        seller.setAddress(sellerContainer.getAddress());
        seller.setSellerId(sellerContainer.getId());
        seller.setBrandName(sellerContainer.getBrandName());
        seller.setEmail(sellerContainer.getUser().getEmail());
        seller.setCompanyName(sellerContainer.getCompanyName());
        seller.setFullName(sellerContainer.getUser().getFullName());
        seller.setPhoneNumber(sellerContainer.getUser().getPhoneNumber());
    }

    private void deleteSeller(String sellerId) {
        sellerRepository.findBySellerId(sellerId).ifPresent(sellerRepository::delete);
    }
}
