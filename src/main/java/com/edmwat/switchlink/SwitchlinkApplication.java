package com.edmwat.switchlink;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.edmwat.switchlink.models.Account;
import com.edmwat.switchlink.services.CashService;

@SpringBootApplication
public class SwitchlinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwitchlinkApplication.class, args);
	}
	@Bean
	CommandLineRunner run(CashService service) {
		return args ->{
			service.saveAccount(new Account(1l,"brenda@gmail.com","Brenda","67891",7800d));
			service.saveAccount(new Account(2l,"edmwat@gmail.com","edward","12345",2300d));			 
		};
	}
}
