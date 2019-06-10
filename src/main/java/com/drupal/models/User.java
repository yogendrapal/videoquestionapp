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
	private String[] interests;
	private int age;
	private String phone;
	
	
	public User() {
	}
	
	public String[] getInterests() {
		return interests;
	}
	public void setInterests(String[] interests) {
		this.interests = interests;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public User(String name, String email, String password, int age,
			String phone, String[] interests) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
		this.interests = interests;
		this.age = age;
		this.phone = phone;
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
