package com.drupal.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.drupal.models.VerificationToken;


public interface VerificationTokenRepo extends MongoRepository<VerificationToken, String>{
	/**
	 * Finds the verification token associated to the user.
	 * 
	 * @param userId the id of the user.
	 * @return the verification token associated to the user.
	 */
	VerificationToken findByUserId(String userId);
	/**
	 * Finds the verification token associated to the token.
	 * 
	 * @param token the token associated to the verification token.
	 * @return the verification token associated to the token.
	 */
	VerificationToken findByToken(String token);
}
