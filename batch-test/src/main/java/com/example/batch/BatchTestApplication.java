package com.example.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.config.EnableIntegration;

@EnableIntegration
@EnableBatchIntegration
@EnableBatchProcessing
@EnableJpaRepositories
@SpringBootApplication (scanBasePackages = {
        "com.example.batchmaster",
        "com.example.batchslave"
})
public class BatchTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchTestApplication.class, args);
    }

}
