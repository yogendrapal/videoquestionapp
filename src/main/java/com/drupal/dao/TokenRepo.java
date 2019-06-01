package com.drupal.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.drupal.models.Token;

public interface TokenRepo extends MongoRepository<Token, String>{
	
}
