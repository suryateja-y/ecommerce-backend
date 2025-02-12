package com.academy.projects.ecommerce.productonboardingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProductOnboardingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductOnboardingServiceApplication.class, args);
	}

}
