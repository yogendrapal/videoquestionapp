package com.drupal.models;

public class Interests {
	String[] interests;

	/**
	 * Gets the interests. 
	 * @return An array of Strings of interests. 
	 */
	public String[] getInterests() {
		return interests;
	}

	/**
	 * Sets the interests.
	 * @param interests The interests used to set interests.
	 */
	public void setInterests(String[] interests) {
		this.interests = interests;
	}

	/**
	 * Constructor for the Interests object.
	 * @param interests The array of Strings of interests used to initialize the Interests object.
	 */
	public Interests(String[] interests) {
		this.interests = interests;
	}

	/**
	 * Default constructor of the Interests object.
	 */
	public Interests() {
	}
	
	
}
