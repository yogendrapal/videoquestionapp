package com.drupal.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Answer {
	@Id
	String id;
	String path;
	String userId;
	String questionId;
	
	/**
	 * Gets the id of the answer.
	 * 
	 * @return The id of the answer. 
	 */
	public String getId() {
		return id;
	}
	/**
	 * Associates the answer with a particular id.
	 * 
	 * @param id The id of the answer 
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * Gets the path of the answer video. 
	 * 
	 * @return The path of the answer video.
	 */
	public String getPath() {
		return path;
	}
	/**
	 * Sets the path of the answer.
	 * @param path The path of the answer.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * Gets the id of the user who uploaded the answer.
	 * @return The id of the user who uploaded the answer.
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * Sets the userId associated with the answer.
	 * @param userId The id to be associated with the answer.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * Constructor which initializes the Answer object.
	 * @param path The path where the answer video is stored.
	 * @param userId The id of the user who uploaded the answer video.
	 * @param questionId The id of the question for which the answer was uploaded.
	 */
	public Answer(String path, String userId, String questionId) {
		super();
		this.path = path;
		this.userId = userId;
		this.questionId = questionId;
	}
	/**
	 * Gets the id of the question for which the answer was uploaded.
	 * @return The id of the question for which the answer was uploaded.
	 */
	public String getQuestionId() {
		return questionId;
	}
	/**
	 * Sets the question id of the Answer.
	 * @param questionId The id of the question for which the answer was uploaded.
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	
	
	
	
	
}
