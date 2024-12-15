package Neoflex_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "Neoflex_bank.MS_deal.db_pgsql.entity")
@EnableJpaRepositories("Neoflex_bank.MS_deal.db_pgsql.repository")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("App started!");
	}

}
