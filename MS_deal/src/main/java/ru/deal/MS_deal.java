package ru.deal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "ru.deal.db_pgsql.entity")
@EnableJpaRepositories("ru.deal.db_pgsql.repository")
@EnableFeignClients
public class MS_deal {
	public static void main(String[] args) {
		SpringApplication.run(MS_deal.class, args);
	}

}