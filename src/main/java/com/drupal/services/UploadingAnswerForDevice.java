package com.drupal.services;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.drupal.dao.AnswerRepo;
import com.drupal.models.Answer;
import com.drupal.models.Video;


@Service
public class UploadingAnswerForDevice {
  
	@Value("${env.rasp_ip}")
	private String raspIp;
	
	@Value("${env.rasp_port}")
	private String raspPort;
	
	@Autowired
	FileStorageService fileStorageService;
	
	AnswerRepo answerRepo;
	
	public  void uploadingAnswerToDeviceServer(Answer file,String questionId,String instituteId) {
//		String vidPath = video.getPath();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, Object> body
		  = new LinkedMultiValueMap<>();
		Resource vidResource = fileStorageService.loadFileAsResource(file.getPath(), "answer");
		
		body.add("file", vidResource);
//		body.add("questionId", questionId); // Question id is query param
//		body.add("instituteId" , instituteId );
		
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		 
		String serverUrl = "http://"+ raspIp+":"+ raspPort+"/answer/add/"+questionId;
		 
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response  = restTemplate.postForEntity(serverUrl, requestEntity, String.class);
		String responseBody = response.getBody();
		System.out.println(responseBody);
		if(response.getStatusCode()==HttpStatus.OK) {
			System.out.println("success");
		}
		else {
			System.out.println("failure");
		}
//		if(responseBody=="success") {
//			System.out.println("success");
//		}
//		else {
//			System.out.println("failure");
//		}
			
	}
	
}
