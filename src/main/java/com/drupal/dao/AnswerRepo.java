package com.drupal.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.drupal.models.Answer;

public interface AnswerRepo extends MongoRepository<Answer, String>{
	/**
	 * Finds the answer using the path parameter.
	 * @param path The path used to find the answer.
	 * @return The answer that matches the path or else null.
	 */
	Answer findByPath(String path);
	/**
	 * Finds all the answers answered by the user with id - userId 
	 * 
	 * @param userId The id of the user to find all the answers associated with the user.
	 * @return List of Answer objects representing answers answered by the user with id - userId.
	 */
	List<Answer> findByUserId(String userId);
	/**
	 * Finds all the answers answered for question with id - questionId.
	 * 
	 * @param questionId The id of the question to find all the answers for that question. 
	 * @return List of Answer objects representing answers answered for the question with id - questionId.
	 */
	List<Answer> findByQuestionId(String questionId);

}
