package com.drupal.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Token {
	@Id
	String id;
	long expiryTime;
	@Override
	public String toString() {
		return "Token [id=" + id + ", expiryTime=" + expiryTime + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(long expiryTime) {
		this.expiryTime = expiryTime;
	}
	
}
