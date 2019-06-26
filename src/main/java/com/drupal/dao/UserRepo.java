package com.drupal.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.drupal.models.User;

//@RepositoryRestResource(collectionResourceRel = "students", path="students")
/**
 * The interface to work with the Users Database.
 * 
 * @author pratik
 * @author henil
 * @author shweta
 * @author sai
 * @author dweekshita
 * @author bhagya
 *
 *@see User
 */
public interface UserRepo extends MongoRepository<User, String>{
	/**
	 * Finds the users with name: <b>name</b>
	 * Returns a list of users with the name provided.
	 * If there is no such user, the list is empty.
	 * @param name the name of the user
	 * @return the list of all the users with name
	 * 
	 * @see User
	 */
	List<User> findByName(String name);
	/**
	 * Find the user in the db with email.
	 * Returns null if there is no such user
	 * 
	 * @param email the email of the user 
	 * @return the User whose email is <i>email</i>
	 * 
	 * @see User
	 */
	User findByEmail(String email);
}