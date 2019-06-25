package com.drupal.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.drupal.models.Video;

public interface VideoRepo extends MongoRepository<Video, String>{
	/**
	 * Finds the video with the given path.
	 * 
	 * @param path the path of the video.
	 * @return the video at the given path.
	 */
	Video findByPath(String path);
	/**
	 * Finds all the videos associated to the given user.
	 * 
	 * @param userId the id of the user. 
	 * @return the list of videos associated to the user.
	 */
	List<Video> findByUserId(String userId);
}
