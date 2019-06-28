package com.drupal.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file")
@Component
public class FileStorageProperties {

	/**
	 * Stores the path where uploaded questions of the user are to be saved
	 */
	private String uploadDir;
	/**
	 * Stores the path where profile picture of the user is to be saved
	 */
	private String profilePicDir;
	/**
	 * Stores the path where answers of the user are to be saved
	 */
	private String answersDir;

	/**
	 * Path where the answers of the user are stored is returned
	 * 
	 * @return path to stored answers
	 */
	public String getAnswersDir() {
		return answersDir;
	}

	/**
	 * Sets path where the answers of the user are to be stored
	 * 
	 * @param answersDir takes path where answers are stored
	 */
	public void setAnswersDir(String answersDir) {
		this.answersDir = answersDir;
	}

	/**
	 * @return path to the profile picture
	 */
	public String getProfilePicDir() {
		return profilePicDir;
	}

	/**
	 * Sets path where profile pictures are to be stored
	 * 
	 * @param profilePicDir takes path where profile picture gets stored
	 */
	public void setProfilePicDir(String profilePicDir) {
		this.profilePicDir = profilePicDir;
	}

	/**
	 * @return path to the uploaded questions of the user
	 */
	public String getUploadDir() {
		return uploadDir;
	}

	/**
	 * Sets path where question videos are to be stored
	 * 
	 * @param uploadDir takes path where question videos are stored
	 */
	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
}