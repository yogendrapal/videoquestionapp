package com.drupal.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Video {
	/**
	 * The unique id assoiated with each video.
	 */
	@Id
	String id;

	/**
	 * The topic related to video
	 */
	String topic;

	/**
	 * The gender of the person who recorded the video
	 */
	String gender;

	/**
	 * The emotions of the user who recorded the video
	 */
	List<String> emotions;

	public Video(String id, String topic, String gender, List<String> emotions, boolean isDevice, String path,
			String userId, List<String> tags) {
		super();
		this.id = id;
		this.topic = topic;
		this.gender = gender;
		this.emotions = emotions;
		this.isDevice = isDevice;
		this.path = path;
		this.userId = userId;
		this.tags = tags;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<String> getEmotions() {
		return emotions;
	}

	public void setEmotions(List<String> emotions) {
		this.emotions = emotions;
	}

	/**
	 * Whether video is from a device.
	 */
	boolean isDevice = false;
	/**
	 * The path of the video.
	 */
	String path;
	/**
	 * The id of the user who recorded the video.
	 */
	String userId;
	/**
	 * All the topics related to video.
	 */
	List<String> tags;

	/**
	 * Constructor of the class Video
	 * 
	 * @param path   The path of the video.
	 * @param userId The userId of the video.
	 * @param tags   The topics of the video.
	 */
	public Video(String path, String userId, List<String> tags) {
		this.path = path;
		this.userId = userId;
		this.tags = tags;
	}

	/**
	 * The userId associated to the video is returned.
	 * 
	 * @return the userId associated to the video.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the userId associated to the video.
	 * 
	 * @param userId the value that must be given to userId of the video.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * The Id associated to the video is returned.
	 * 
	 * @return the Id associated to the video.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the Id associated to the video.
	 * 
	 * @param id the value that must be given to Id of the video.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The path associated to the video is returned.
	 * 
	 * @return the path associated to the video.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path associated to the video.
	 * 
	 * @param path the value that must be given to path of the video.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * The tags associated to the video is returned.
	 * 
	 * @return the tags associated to the video.
	 */
	public List<String> getTags() {
		return tags;
	}

	public Video() {
		super();
	}

	public boolean isDevice() {
		return isDevice;
	}

	public void setDevice(boolean isDevice) {
		this.isDevice = isDevice;
	}

	/**
	 * Sets the tags associated to the video.
	 * 
	 * @param tags the value that must be given to tags of the video.
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * Returns the topic of the video.
	 * 
	 * @return the topic of the video
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Sets the topic of the video.
	 * 
	 * @param topic the new topic of the video
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

}