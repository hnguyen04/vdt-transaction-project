package com.example.complain_service;

import org.springframework.boot.SpringApplication;

public class TestComplainServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ComplainServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
