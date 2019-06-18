package com.drupal.services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.drupal.dao.VideoRepo;
import com.drupal.models.Video;


@Service
public class VideoTagsFetcherService {
	@Autowired
	FileStorageService fileStorageService;
	
	@Autowired
	VideoRepo videoRepo;
	
	public void fetchDataFor(Video video) {
		String vidPath = video.getPath();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body
		  = new LinkedMultiValueMap<>();
		Resource vidResource = fileStorageService.loadFileAsResource(vidPath, "video");
		
		body.add("video", vidResource);
		body.add("id", 1);
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		 
		String serverUrl = "http://10.196.13.169:3000/upload";
		 
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response  = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
		String responseBody = response.getBody();
		System.out.println(responseBody);
		JSONObject jsonResponse = new JSONObject(responseBody);
		JSONArray emotions  = jsonResponse.getJSONArray("Subjects");
		video.setTags((List<String>)((Object)emotions.toList()));
		videoRepo.save(video);
		
		
		
		
	}
	
}
