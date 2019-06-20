package com.drupal.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Institute {
	@Id
	String id;
	String name;
	String address;
	String phone;
	String password;
	String email;
	boolean emailVerified = false;
	public Institute() {
		
	}
	
	
	public Institute(String name, String address, String phone, String password, String email) {
		super();
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.password = password;
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
	public boolean isEmailVerified() {
		return emailVerified;
	}


	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Institute(String name, String address, String phone, String password) {
		super();
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return "Institute [Id=" + id + ", name=" + name + ", address=" + address + ", phone=" + phone + "]";
	}

	
	
}
