package com.drupal.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.drupal.models.User;

//@RepositoryRestResource(collectionResourceRel = "students", path="students")
public interface UserRepo extends MongoRepository<User, String>{
	List<User> findByName(String name);
	User findByEmail(String email);
}