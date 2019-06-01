package com.drupal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentRestApiApplication {

	public static final String SECRET_KEY = "This is secret";
	public static void main(String[] args) {
		SpringApplication.run(StudentRestApiApplication.class, args);
	}

}
