package com.drupal.models;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class User {
	@Id
	private String id;
	private String name;
	private String password;
	private String email;
	private boolean isEmailVerified;
	public User(String name, String email, String password) {
		this.name = name;
		this.email  = email;
		this.password = password;
		this.isEmailVerified = false;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Student [name=" + name + ", password=" + password + ", email=" + email + "]";
	}

	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}
	
}
