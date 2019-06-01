package com.drupal.models;

public class StudentHiddenPassword {
	private String id;
	private String name;
	private String email;

	public StudentHiddenPassword(Student s) {
		if (s != null) {
			this.id = s.getId();
			this.email = s.getEmail();
			this.name = s.getName();
		}
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "StudentHiddenPassword [id=" + id + ", name=" + name + ", email=" + email + "]";
	}

}
