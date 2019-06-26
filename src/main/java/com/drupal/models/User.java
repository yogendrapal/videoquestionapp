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
	private String profilePic;
	
	
	
	/**
	 * Gets the profilePic location of this User within storage
	 * 
	 * If this user doesn't have a profile pic, returns null instead
	 * @return the profilepic location of this user
	 */
	public String getProfilePic() {
		return profilePic;
	}

	/**
	 * Sets/Updates the profile-pic location of this user to profilePic.
	 * 
	 * @param profilePic the new profile pic location of the user's profile pic in the storage
	 */
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	/**
	 * Creates a new User with no details.
	 */
	public User() {
	}
	
	/**
	 * Returns the interests of this user if he has any.
	 * 
	 * Return null if the user has no interest.
	 * 
	 * @return the interests of this user
	 * @see Interests
	 */
	public String[] getInterests() {
		return interests;
	}
	/**
	 * Sets/Updates the interests of this user to interests
	 * 
	 * @param interests the new interests of the user
	 * @see Interests
	 */
	public void setInterests(String[] interests) {
		this.interests = interests;
	}
	/**
	 * Returns the age of the user which he updated the last time
	 * 
	 * Note: The age is not automatically updated and the user has to manually update it
	 * 
	 * @return the registered age of the user
	 */
	public int getAge() {
		return age;
	}
	/**
	 * Sets/Updates the age of the user.
	 * 
	 * Note: The age is not automatically updated and the user has to manually update it
	 * 
	 * @param age the age of the user as mentioned by him/her
	 * @see getAge()
	 */
	public void setAge(int age) {
		this.age = age;
	}
	/**
	 * Returns the phone number of the user.
	 * 
	 * @return the phone number of the user
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * Sets/Updates the phone number of the user. 
	 * 
	 * @param phone the new phone-number to be updated with
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * Creates a new User with the details provided
	 * 
	 * @param name the name of this user
	 * @param email the email of this user
	 * @param password the encrypted password of this user
	 * @param age the age of this user
	 * @param phone the phone of this user
	 * @param interests the interests of this user
	 */
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
	/**
	 * Returns the unique id provided to the User.
	 * 
	 * This id is generated automatically and is unique for each user.
	 * 
	 * @return the id of the user
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets the id of the user with this <b>id</b>
	 * 
	 * Note: This method is not intended to be used manually as it may result in overlapping ids.
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
	/**
	 * Returns the name of this user.
	 * 
	 * @return the name of this user
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets/Updates the name of this user.
	 * @param name the new name of this user
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Returns the password of this user.
	 * 
	 * @return the encrypted password of this user.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Sets/Updates the password of this user.
	 * 
	 * No encryption is done within this method.
	 * The newPassword to be entered should be encrypted
	 * @param password the new encrypted password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * Returns the email of this user.
	 * 
	 * @return the email of this user
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Sets/Updates the email of this user.
	 * 
	 * Note: This method is not intended to be used manually as an email also uniquely identifies the user.
	 * Changing the user may result in loss of the user account.
	 * @param email the new email of this user
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", password=" + password + ", email=" + email + "]";
	}

	/**
	 * Tells whether this user's email is verified.
	 * 
	 * @return true if email of this user is verified, otherwise false
	 */
	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	/**
	 * Changes the email verification status of this user.
	 * 
	 * @param isEmailVerified the new email-verification-status.
	 */
	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}
	
}
