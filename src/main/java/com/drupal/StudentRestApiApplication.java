package com.drupal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class StudentRestApiApplication extends SpringBootServletInitializer{

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
