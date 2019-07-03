package com.drupal.services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * A service which fetches tags for a video by using another api.
 * 
 * This service is supposed to be run in a thread as it is time intensive.
 * 
 * @author pratik,henil,sai,Shweta
 *
 */
@Service
public class VideoTagsFetcherService {
	/**
	 * 
	 */
	@Autowired
	FileStorageService fileStorageService;

	/**
	 * 
	 */
	@Autowired
	VideoRepo videoRepo;

	@Value("${env.tags_fetch_ip}")
	private String tagsFetchIp;

	@Value("${env.tags_fetch_port}")
	private String tagsFetchPort;

	/**
	 * Fetches the data(tags) for the video specified by <b>video</b>.
	 * 
	 * <p>
	 * It finds the actual video in storage using the video, sends this video to another public api 
	 * which is supposed to provide the tags for the video.
	 * It receives the tags from the api and update the entry for the video with the tags.
	 * 
	 * @param video  the Video for which the data has to be fetched
	 * @see Video
	 */
	public void fetchDataFor(Video video) {
		System.out.println("in fetch data for started sending request");
		String vidPath = video.getPath();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body
		  = new LinkedMultiValueMap<>();
		Resource vidResource = fileStorageService.loadFileAsResource(vidPath, "video");
		
		body.add("video", vidResource);
		body.add("id", video.getId());
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		 
		String serverUrl = "http://"+tagsFetchIp+":"+tagsFetchPort+"/drupal/upload";
		 
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response  = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
		System.out.println("response" + response);
		String responseBody = response.getBody();
		System.out.println(responseBody);
		JSONObject jsonResponse = new JSONObject(responseBody);
		JSONArray topic = null;
		try {
		topic = jsonResponse.getJSONArray("Topic");
		}
		catch(Exception e) {
			
		}
		JSONArray keywords = null;
		try {
			keywords= jsonResponse.getJSONArray("keyword");
		}
		catch(Exception e) {
			
		}
		if(keywords!=null) {
		video.setTags((List<String>)((Object)keywords.toList()));
		}
		if(topic != null) {
			video.setTopic(topic.get(0).toString());
		}
//		video.setTopic(topic);
		videoRepo.save(video);
		System.out.println("fetching tags completed");
	}

}
