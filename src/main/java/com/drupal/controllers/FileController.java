package com.drupal.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.drupal.StudentRestApiApplication;
import com.drupal.UploadFileResponse;
import com.drupal.dao.AnswerRepo;
import com.drupal.dao.TokenRepo;
import com.drupal.dao.UserRepo;
import com.drupal.dao.VideoRepo;
import com.drupal.models.Answer;
import com.drupal.models.Token;
import com.drupal.models.User;
import com.drupal.models.Video;
import com.drupal.models.VideoTags;
import com.drupal.services.FileStorageProperties;
import com.drupal.services.FileStorageService;
import com.drupal.services.VideoTagsFetcherService;

@Controller
public class FileController {
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Value("${file.profile-pic-dir}")
	private String profilePicDir;

	@Value("${file.answers-dir}")
	private String answersDir;

	@Autowired
	ApiController apiController;
	
	@Autowired
	AnswerRepo answerRepo;
	
	@Autowired
	TokenRepo tokenRepo;
	
	@Autowired
	FileStorageProperties fileStorageProperties;

	@Autowired
	VideoTagsFetcherService videoTagsFetcherService;

	
	@Autowired
	VideoRepo videoRepo;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	TokenController tokenController;

	@Autowired
	UserController userController;

	@Autowired
	UserRepo userRepo;

	/**
	 * Uploads the file to the server.
	 * 
	 * <p>
	 * Saves the file received in the request in storage and inserts a new record in Video Document for the file.
	 * 
	 * @param file the file sent by the client in the Multipart form
	 * @param tokenId string sent within the request to identify the session of the user
	 * @param res the response returned by this to the client
	 * @param tags the video-tags of the file
	 * @return UploadFileResponse for the file uploaded containing the download-url of the file
	 */
	@PostMapping("/uploadFile")
	@ResponseBody
	public UploadFileResponse uploadFile(@RequestParam("video") MultipartFile file, @RequestPart String tokenId, HttpServletResponse res, @ModelAttribute() VideoTags tags) {
		System.out.println("Uploading");
		System.out.println(tokenId);
		String fileName = fileStorageService.storeFile(file);
		System.out.println(fileName);
		String userId = tokenController.getUserIdFrom(tokenId);
		if (userId == StudentRestApiApplication.NOT_FOUND) {
			try {
				res.sendError(400, "Invalid token or expired token..Login again");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();
		System.out.println(Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize().resolve(fileName));
		String path = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize().resolve(fileName).toString();
		Video v = videoRepo.findByPath(path);
		if (v == null) {
			System.out.println("Saving");
			videoRepo.save(new Video(path, userId, tags.getTags()));
			Thread thread = new Thread(new Runnable() {

				
				@Override
				public void run() {
					System.out.println("fetcher thread started");
					videoTagsFetcherService.fetchDataFor(videoRepo.findByPath(path));
					System.out.println("fetcher thread finishing");
				}
			});
			thread.start();
			//videoTagsFetcherService.fetchDataFor(videoRepo.findByPath(path));
		}
		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}


	/**
	 * Downloads the fileName from the server.
	 * 
	 * <p>
	 * Uses FileStorageService.loadFileAsResource  with <b>question</b> argument internally to load the required resource.
	 * Content-type if the mime-type of the file(if it has one), otherwise <i>application/octet-stream</i>
	 * 
	 * @param fileName the name of the file to download.
	 * @param request the request send by the client
	 * @return the resource requested in request using fileName
	 */
	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName, "question");
		System.out.println(resource.toString());
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	/**
	 * Downloads the video-answer denoted by fileName from the server.
	 * 
	 * <p>
	 * Uses FileStorageService.loadFileAsResource  with <b>answer</b> argument internally to load the required resource.
	 * Content-type if the mime-type of the file(if it has one), otherwise <i>application/octet-stream</i>
	 * 
	 * @param fileName the name of the file to download.
	 * @param request the request send by the client
	 * @return the answer video requested in request using fileName
	 */
	@GetMapping("/downloadAnswer/{fileName:.+}")
	public ResponseEntity<Resource> downloadAnswer(@PathVariable String fileName, HttpServletRequest request) {
		Resource resource = fileStorageService.loadFileAsResource(fileName, "answer");
		System.out.println(resource.toString());
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
		

	/**
	 * Downloads the Profile picture denoted by fileName from the server.
	 * 
	 * <p>
	 * Uses FileStorageService.loadFileAsResource with <b>image</b> argument internally to load the required resource.
	 * Content-type if the mime-type of the file(if it has one), otherwise <i>application/octet-stream</i>
	 * 
	 * 
	 * @param fileName the name of the file to download.
	 * @param request the request send by the client
	 * @return the answer video requested in request using fileName
	 */
	@GetMapping("/getProfilePic/{fileName:.+}")
	public ResponseEntity<Resource> getProfilePic(@PathVariable String fileName, HttpServletRequest request) {
		Resource resource = fileStorageService.loadFileAsResource(fileName, "image");
		System.out.println(resource.toString());
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

	}
	
	/**
	 * Changes the profile pic of a user denoted by tokenId
	 * 
	 * <p>
	 * Fist saves the pic in the file system
	 * Identifies the user from the tokenId and updates his data in the db to point to new pic
	 * 
	 * @param tokenId string to identify the session of the user
	 * @param res the response returned by this to the client
	 * @param pic the file sent in the multipart form by the client to be saved as profile pic
	 * @return the success or error JSON String 
	 * @throws IOException
	 */
	@RequestMapping(path = "/uploadProfilePic", method = RequestMethod.POST)
	@ResponseBody
	public String uploadProfilePic(@RequestPart("pic") MultipartFile pic, @RequestPart String tokenId, HttpServletResponse res) throws IOException {
		System.out.println("profilePicDir: "+ profilePicDir);
		Token token = tokenRepo.findById(tokenId).orElse(null);
		if(token == null) {
			res.sendError(400, "Invalid token id");
			return "{\"Error\":\"Invalid token id\"}";
		}
		String userId = token.getUserId();
		String extension = pic.getContentType().substring(pic.getContentType().lastIndexOf('/') + 1);
//		String extension = "png";
		String fileName = userId + "." + extension;
		System.out.println(fileName);
		Path targetLocation = Paths.get(profilePicDir).toAbsolutePath().normalize();
		targetLocation = targetLocation.resolve(fileName);
		System.out.println(targetLocation.toString());
		if (targetLocation.toFile().exists()) {
			System.out.println("File already exists....overwriting");
		}
		try {
			Files.copy(pic.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			User user = userRepo.findById(userId).orElse(null);
			user.setProfilePic(targetLocation.toString());
			userRepo.save(user);
			return "{\"Success\":\"Profile Pic uploaded\"}";
		} catch (IOException e) {
			res.sendError(500, "Unable to save pic");
			return "{\"Error\":\"Profile Pic not uploaded\"}";
		}
	}

	/**
	 * Uploads the answer file for questionName
	 * 
	 * 
	 * 
	 * @param file the answer to be uploaded
	 * @param tokenId String to identify the current user session
	 * @param res the http response sent to the client
	 * @param questionName the name of the question for which answer is uploaded
	 * @return the success or error message
	 */
	@RequestMapping("uploadAnswer")
	@ResponseBody
	public String uploadAnswerVideo(@RequestPart("video") MultipartFile file, @RequestPart String tokenId,
			HttpServletResponse res, String questionName) {
		System.out.println("answer uploading, "+ file);
		Token token = tokenRepo.findById(tokenId).orElse(null);
		if (token == null) {
			System.out.println("token is null");
			try {
				res.sendError(400, "Invalid token id");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "{\"Error\":\"Invalid token id\"}";
		}
		String userId = tokenController.getUserIdFrom(tokenId);
		if (userId == StudentRestApiApplication.NOT_FOUND) {
			try {
				System.out.println("token is invalid");
				res.sendError(400, "Invalid token or expired token..Login again");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "{\"Error\":\"Invalid token id\"}";
		}
		String questionPath = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize()
				.resolve(questionName).toString();
		Video question = videoRepo.findByPath(questionPath);
		if (question == null) {
			System.out.println(questionName+ " doesnt exist");
			try {
				res.sendError(400, "Question doesn't exist");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "{\"Error\":\"Question doesn't exist\"}";
		}
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path targetLocation = Paths.get(answersDir).toAbsolutePath().normalize();
		targetLocation = targetLocation.resolve(fileName);
		System.out.println(targetLocation.toString());
		if (targetLocation.toFile().exists()) {
			System.out.println("File already exists....overwriting");
		}
		try {
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			String path = Paths.get(fileStorageProperties.getAnswersDir()).toAbsolutePath().normalize()
					.resolve(fileName).toString();
			Answer ans = answerRepo.findByPath(path);
			if (ans == null) {
				System.out.println("Saving");
				answerRepo.save(new Answer(path, userId, question.getId()));
				return "{\"Success\":\"Answer uploaded successfully\"}";
			}
		} catch (IOException e) {
			try {
				res.sendError(500, "Unable to upload answer");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return "{\"Error\":\"Answer not uploaded\"}";
		}
		return null;
	}
	
	/**
	 * Gives the user details for the ansName
	 * 
	 * 
	 * @param ansName the name of the answer file for which user details is to return
	 * @param res the http response sent to the client
	 * @return the user details who has answered the ansName
	 */
	@GetMapping(path="getUserFromAnswer", produces = "application/json")
	@ResponseBody
	public String getUserFromAnswer(@RequestParam String ansName, HttpServletResponse res) {
		String answerPath = Paths.get(fileStorageProperties.getAnswersDir()).toAbsolutePath().normalize()
				.resolve(ansName).toString();
		Answer ans  = answerRepo.findByPath(answerPath);
		String userEmail = userRepo.findById(ans.getUserId()).orElse(null).getEmail();
		return apiController.getProfileDetails(userEmail, res);
	}
}
