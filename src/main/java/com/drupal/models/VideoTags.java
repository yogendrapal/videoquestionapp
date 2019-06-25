package com.drupal.models;

import java.util.List;

public class VideoTags {
	
	
	/**
	 * Stores the tags associated with the video.
	 */
	private List<String> tags;

	/**
	 * Gets the tags associated with the video.
	 * @return List of all tags related to the video.
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * Sets the tags for the video.
	 * @param tags The list of string tags to be associated with the video. 
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * Constructor 
	 * @param tags Used to initialize the VideoTags object. 
	 */
	public VideoTags(List<String> tags) {
		super();
		this.tags = tags;
	}

	
	
	
}
