package com.example.complain_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.complain_service.client")

public class ComplainServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(ComplainServiceApplication.class, args);
	}

}
