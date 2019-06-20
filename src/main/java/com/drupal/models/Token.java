package com.drupal.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Token {
	@Id
	String id;
	String userId;

	

	@Override
	public String toString() {
		return "Token [id=" + id + ", userId=" + userId + "]";
	}


	public Token(String userId) {
		super();
		this.userId = userId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Token() {
		
	}
}
