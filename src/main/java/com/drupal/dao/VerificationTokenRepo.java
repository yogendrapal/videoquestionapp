package com.drupal.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.drupal.models.VerificationToken;


public interface VerificationTokenRepo extends MongoRepository<VerificationToken, String>{
	VerificationToken findByUserId(String userId);
	VerificationToken findByToken(String token);
}
