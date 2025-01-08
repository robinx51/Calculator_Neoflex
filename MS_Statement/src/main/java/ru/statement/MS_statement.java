package ru.statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MS_statement {
    public static void main(String[] args) {
        SpringApplication.run(MS_statement.class, args);
    }
}