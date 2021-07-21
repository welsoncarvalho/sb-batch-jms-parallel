package com.example.batchmaster;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class BatchMasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchMasterApplication.class, args);
	}

}
