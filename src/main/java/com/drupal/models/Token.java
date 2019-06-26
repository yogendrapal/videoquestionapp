package com.drupal.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * The token which identifies each session uniquely.
 * 
 * If a new log-in is made, a new token is created in DB to maintain the new session.
 * This token is used for authentication instead of username and password.
 * Each user can have only one token present.
 * If the user/institute logs out, the token is deleted.
 * 
 * @author pratik
 * @author sai
 * @author henil
 * @author shweta
 * @author bhagya
 * @author dweekshita
 *
 *@see User
 *@see Institute
 */
@Document
public class Token {
	/**
	 * The unique id of this token.
	 */
	@Id
	String id;
	
	/**
	 * The user-id of the User/Institute with whom this token is associated.
	 * 
	 * @see User
	 * @see Institute
	 */
	String userId;

	

	/**
	 *
	 */
	@Override
	public String toString() {
		return "Token [id=" + id + ", userId=" + userId + "]";
	}


	/**
	 * Sets the user-id to this token with whom this token is associated with
	 * 
	 * @param userId the user-id of the person with whom this token is associated
	 */
	public Token(String userId) {
		super();
		this.userId = userId;
	}


	/**
	 * Returns the user-id of the user/institute with which this token is associated.
	 * 
	 * @return the user-id of the associated user
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * Sets the user-id of the user/institute with which this token is associated.
	 * 
	 * @param userId the user-id of the user/institute for whom the token is created
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
	 * Returns the unique id of this token.
	 * 
	 * @return the id of this token
	 */
	public String getId() {
		return id;
	}


	/**
	 * Sets the unique id of this token.
	 * 
	 * Note: This method is not intended to be used manually as it may result in overlapping ids.
	 * @param id the unique id of this token
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * Creates a new empty token.
	 * 
	 * Note: the constructor is intended for internal operations only.
	 * 
	 */
	public Token() {
		
	}
}
