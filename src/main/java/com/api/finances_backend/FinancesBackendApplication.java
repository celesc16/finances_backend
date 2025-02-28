package com.api.finances_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class FinancesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancesBackendApplication.class, args);
	}

}
