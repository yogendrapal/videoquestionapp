package com.drupal.controllers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.drupal.FileStorageProperties;
import com.drupal.FileStorageService;
import com.drupal.StudentRestApiApplication;
import com.drupal.UploadFileResponse;
import com.drupal.dao.UserRepo;
import com.drupal.dao.VideoRepo;
import com.drupal.models.User;
import com.drupal.models.Video;

@Controller
public class FileController {
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	@Autowired
	FileStorageProperties fileStorageProperties;

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
	public UploadFileResponse uploadFile(@RequestParam("video") MultipartFile file, @RequestPart String tokenId, HttpServletResponse res) {
		System.out.println("Uploading");
		System.out.println(tokenId);
		String fileName = fileStorageService.storeFile(file);

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
			videoRepo.save(new Video(path, userId));
		}
		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}

	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
			@RequestPart String tokenId, HttpServletResponse res) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file, tokenId, res)).collect(Collectors.toList());
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

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
}
