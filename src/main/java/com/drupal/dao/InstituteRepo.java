package com.drupal.dao;

import com.drupal.models.Institute;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstituteRepo extends MongoRepository<Institute,String>{
	Institute findByEmail(String email);
}
