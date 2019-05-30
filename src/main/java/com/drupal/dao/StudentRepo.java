package com.drupal.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.drupal.models.Student;

//@RepositoryRestResource(collectionResourceRel = "students", path="students")
public interface StudentRepo extends MongoRepository<Student, Integer>{
	List<Student> findByName(String name);
	List<Student> findByEmail(String email);
}