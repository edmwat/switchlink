package com.edmwat.switchlink;


import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.edmwat.switchlink.models.Account;
import com.edmwat.switchlink.services.CashService;

@SpringBootApplication
public class SwitchlinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwitchlinkApplication.class, args);
	}
	/*@Bean 
	CommandLineRunner run(CashService service) {
		return args ->{
			service.saveAccount(new Account(1l,"brenda@gmail.com","Brenda","67891",7800d));
			service.saveAccount(new Account(2l,"edmwat@gmail.com","edward","12345",2300d));	
			service.saveAccount(new Account(3l,"edmwat@gmail.com","matu","54321",10000d));	
		};  
	}*/
	/*
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}*/
}
