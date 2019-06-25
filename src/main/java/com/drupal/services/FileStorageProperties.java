package com.drupal.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file")
@Component
public class FileStorageProperties {
    
	private String uploadDir;
    private String profilePicDir;
    private String answersDir;
    
    public String getAnswersDir() {
		return answersDir;
	}

	public void setAnswersDir(String answersDir) {
		this.answersDir = answersDir;
	}

	public String getProfilePicDir() {
		return profilePicDir;
	}

	public void setProfilePicDir(String profilePicDir) {
		this.profilePicDir = profilePicDir;
	}

	public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}