package com.academy.projects.ecommerce.cartmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class CartManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartManagementServiceApplication.class, args);
	}

}
