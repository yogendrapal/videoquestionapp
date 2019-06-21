package com.drupal.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.drupal.models.Answer;

public interface AnswerRepo extends MongoRepository<Answer, String>{
	Answer findByPath(String path);
	List<Answer> findByUserId(String userId);
	List<Answer> findByQuestionId(String questionId);

}
