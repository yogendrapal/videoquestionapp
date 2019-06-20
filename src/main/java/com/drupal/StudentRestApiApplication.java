package com.drupal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


// Exclude is not necessary here as the default seurity configuration is changed
@SpringBootApplication (exclude = { SecurityAutoConfiguration.class}) // TODO remove the exclude tag if any error related to security(requests not working (forbidden)) occurs
public class StudentRestApiApplication extends SpringBootServletInitializer{

	
	public static enum RegistrationTypes{
		institute,user
	};
	public static final String NOT_FOUND = "NOT FOUND";
	public static final String SECRET_KEY = "This is secret";
	
	public static void main(String[] args) {
		SpringApplication.run(StudentRestApiApplication.class, args);
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(StudentRestApiApplication.class);
	}

	
}
