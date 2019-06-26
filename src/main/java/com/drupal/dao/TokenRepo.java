package com.drupal.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.drupal.models.Token;

/**
 * The interface to work with the Tokens DB
 * 
 * @author pratik
 * @author shweta
 * @author sai
 * @author henil
 * @author bhagya
 * @author dweekshita
 * @see Token
 */
public interface TokenRepo extends MongoRepository<Token, String>{
	/**
	 * Fetches the token for using the userId.
	 * 
	 * Returns null if there is no token for userId.
	 * Looks into the token database and finds the unique token which is associated with User/Institute having userid.
	 * <p>
	 * Note: it is guaranteed that there is only one or no token for one user-id.
	 * </p>
	 * @param userId
	 * @return the token associated with userId
	 */
	Token findByUserId(String userId);
}
