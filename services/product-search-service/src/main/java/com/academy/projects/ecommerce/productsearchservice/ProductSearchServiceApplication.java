package com.academy.projects.ecommerce.productsearchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductSearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductSearchServiceApplication.class, args);
	}

}
