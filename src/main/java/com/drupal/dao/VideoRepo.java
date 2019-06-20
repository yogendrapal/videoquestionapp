package com.drupal.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.drupal.models.Video;

public interface VideoRepo extends MongoRepository<Video, String>{
	Video findByPath(String path);
	List<Video> findByUserId(String userId);
}
