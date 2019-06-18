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
import com.drupal.dao.TokenRepo;
import com.drupal.dao.UserRepo;
import com.drupal.dao.VideoRepo;
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

	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
			@RequestPart String tokenId, HttpServletResponse res) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file, tokenId, res, null)).collect(Collectors.toList());
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName, "video");
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
		String extension = pic.getContentType().substring(pic.getContentType().lastIndexOf('/')+1);
//		String extension = "png";
		String fileName = userId+"."+extension;
		System.out.println(fileName);
		Path targetLocation = Paths.get(profilePicDir).toAbsolutePath().normalize();
		targetLocation = targetLocation.resolve(fileName);
		System.out.println(targetLocation.toString());
		 if(targetLocation.toFile().exists()) {
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
}
