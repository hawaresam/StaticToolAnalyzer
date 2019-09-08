package com.philips.staticanalysis.gatingapp;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatingAppApplication {
	public static void main(String[] args) {
		BasicConfigurator.configure(); 
		SpringApplication.run(GatingAppApplication.class, args);
	}
}
