package com.drupal.models;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VerificationToken {
	/**
	 * The time after which the verification links expires.
	 */
	private static final int EXPIRATION = 60 * 24;

	/**
	 * The id associated with every token.
	 */
	@Id
	String id;
	/**
	 * The user associated with the token.
	 */
	String userId;
	/**
	 * The tokenId present in the verification url.
	 */
	private String token;
	/**
	 * The date when the token is created.
	 */
	private Date createdDate;
	/**
	 * The date when the token will be expired.
	 */
	private Date expiryDate;

	/**
	 * Calculates the expiry date of the token. 
	 * 
	 * @param expiryTimeInMinutes The time after which the token should be expired.
	 * @return the expiry date of a token.
	 */
	private static Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Timestamp(calendar.getTime().getTime()));
		calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(calendar.getTime().getTime());
	}

	/**
	 * Constructor for the class VerificationToken
	 * 
	 * @param userId The id of the user.
	 * @param token The generated token of the user.
	 */
	public VerificationToken(String userId, String token) {
		this.userId = userId;
		this.token = token;
		this.createdDate = new Date();
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}
	
	
	/**
	 * The id of the verification token is returned.
	 * 
	 * @return The id of the verification token.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id.
	 * 
	 * @param id The value that is to be stored.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The userId associated with the token is returned.
	 * 
	 * @return The user associated with the token.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the userId of the token.
	 * 
	 * @param userId The value of the userId that is to be setted.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	

	/**
	 * To convert the VerificationToken to a string.
	 */
	@Override
	public String toString() {
		return "VerificationToken [id=" + id + ", userId=" + userId + "]";
	}

	/**
	 * The token of the object is returned.
	 * 
	 * @return The token of the object.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token of the object.
	 * 
	 * @param token The value of the token that is to be setted.
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * The date of creation is returned.
	 * 
	 * @return the date of creation of the token.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the date of creation of token.
	 * 
	 * @param createdDate The date that is to be setted. 
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * The expiry date is returned.
	 * 
	 * @return the expiry date of the token.
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	/**
	 * Sets the expiry date of creation of token.
	 * 
	 * @param expiryDate The expiry date that is to be setted. 
	 */
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
}

