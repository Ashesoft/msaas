package com.longrise.msaas.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.longrise.msaas"})
public class MsaasWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsaasWebApplication.class, args);
	}

}
