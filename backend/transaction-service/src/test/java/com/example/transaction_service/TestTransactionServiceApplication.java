package com.example.transaction_service;

import org.springframework.boot.SpringApplication;

public class TestTransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(TransactionServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
