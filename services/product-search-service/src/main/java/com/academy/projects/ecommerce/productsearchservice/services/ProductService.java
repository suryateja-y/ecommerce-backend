package com.academy.projects.ecommerce.productsearchservice.services;

import com.academy.projects.ecommerce.productsearchservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.productsearchservice.exceptions.ProductNotFoundException;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.Action;
import com.academy.projects.ecommerce.productsearchservice.kafka.dtos.ProductActionDto;
import com.academy.projects.ecommerce.productsearchservice.models.Product;
import com.academy.projects.ecommerce.productsearchservice.models.Seller;
import com.academy.projects.ecommerce.productsearchservice.models.Variant;
import com.academy.projects.ecommerce.productsearchservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final IdGenerator idGenerator;

    @Autowired
    public ProductService(ProductRepository productRepository, IdGenerator idGenerator) {
        this.productRepository = productRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Product getByProductId(String productId) {
        return productRepository.findByProductId(productId).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    public void update(ProductActionDto productDto) {
        if(productDto.getAction().equals(Action.DELETE)) {
            this.deleteProduct(productDto.getProduct().getId());
        } else {
            Product productUpdate = productDto.getProduct();
            Product product = productRepository.findByProductId(productUpdate.getId()).orElse(null);
            if (product == null) {
                product = new Product();
                product.setId(idGenerator.getId(Product.SEQUENCE_NAME));
            }
            get(productUpdate, product);
            productRepository.save(product);
        }
    }

    private void get(Product productUpdate, Product product) {
        product.setProductId(productUpdate.getId());
        product.setName(productUpdate.getName());
        product.setDescription(productUpdate.getDescription());
        product.setCategory(productUpdate.getCategory());
        product.setCompany(productUpdate.getCompany());
        product.setBrandName(productUpdate.getBrandName());
        product.setImage(productUpdate.getImage());
        product.setDescription(productUpdate.getDescription());
        product.setDimensionalMetrics(productUpdate.getDimensionalMetrics());
        product.setAttributes(productUpdate.getAttributes());
        product.setVariants(new ArrayList<>());
    }

    private void deleteProduct(String productId) {
        productRepository.findByProductId(productId).ifPresent(productRepository::delete);
    }

    @Override
    public Product update(Product product, Variant variant, Seller seller, BigDecimal unitPrice) {
        return product;
    }

    @Override
    public Product update(Product product) {
        return productRepository.save(product);
    }
}
