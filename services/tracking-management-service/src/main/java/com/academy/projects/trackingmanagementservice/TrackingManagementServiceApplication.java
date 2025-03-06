package com.academy.projects.trackingmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@EnableFeignClients
public class TrackingManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackingManagementServiceApplication.class, args);
	}

}
