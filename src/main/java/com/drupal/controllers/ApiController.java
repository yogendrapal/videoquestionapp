package com.drupal.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drupal.AES;
import com.drupal.StudentRestApiApplication;
import com.drupal.dao.TokenRepo;
import com.drupal.dao.UserRepo;
import com.drupal.dao.VideoRepo;
import com.drupal.models.Token;
import com.drupal.models.User;
import com.drupal.models.Video;

@Controller
public class ApiController {

	@Autowired
	TokenController tokenController;
	
	@Autowired
	TokenRepo tokenRepo;

	@Autowired
	UserController userController;

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	VideoRepo videoRepo;
	
	@Autowired
	FileController fileController;

	@RequestMapping(path = "login", method=RequestMethod.POST , produces = "application/json")
	@ResponseBody
	public String login(@RequestPart String email, @RequestPart String password, HttpServletResponse res) {
		User user = userRepo.findByEmail(email);
		if (user != null) {
			Token t = tokenRepo.findByUserId(user.getId());
			if(t != null) {
				try {
					res.sendError(400, "User already signed in other device");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "";
			}
			else if (AES.encrypt(password, StudentRestApiApplication.SECRET_KEY).equals(user.getPassword())) {
				Token token = tokenController.createToken(user.getId());
				return "{\"Token Id\" : \"" + token.getId() + "\"}"; 
			} else {
				try {
					res.sendError(403, "Incorrect password");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "Incorrect password";
			}

		} else {
			try {
				res.sendError(400, "Email doesn't exist.Please signup first...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(path="signup", method=RequestMethod.POST , produces = "application/json")
	@ResponseBody
	public String signUp(@RequestPart String email, @RequestPart String password, @RequestPart String name,  HttpServletResponse res) {
		if(userRepo.findByEmail(email) == null) {
			User user = userController.postUser(name, email, password);
			return login(email, password, res);
		}
		else {
			return "{\"Error\" : \"User with this email already exist\"}";
		}
	}
	
	@RequestMapping(path="videos", method = RequestMethod.GET)
	@ResponseBody
	public List<Video> getVideos(@RequestPart String tokenId, HttpServletRequest req,  HttpServletResponse res){
		String userId = tokenController.getUserIdFrom(tokenId);
		System.out.println(userId);
		if(userId.equals(StudentRestApiApplication.NOT_FOUND)) {
			try {
				res.sendError(404, "User associated with tokenId not found");
			} catch (IOException e) {
				System.out.println("catched");
				e.printStackTrace();
			}
			return null;
		}
		List<Video> videos = videoRepo.findByUserId(userId);
		return videos;
	}
	
	@RequestMapping(path = "/logout" , method = RequestMethod.POST , produces = "application/json")
	@ResponseBody
	public String loggingOut(@RequestPart String tokenId,@RequestPart String email,HttpServletResponse res) {
		System.out.println("In Logout");
		if(tokenController.deleteToken(tokenId, email)) {
			return "{\"Success\" : \"Successfully Logged Out\"}";
		}
		else return "{\"Error\" : \"Cannot Log Out The User\"}";
	}
	
	@RequestMapping(path="videos/{id}", method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource> getVideo(@PathVariable String id, @RequestPart String tokenId, HttpServletRequest req,  HttpServletResponse res){
		Video v = videoRepo.findById(id).orElse(null);
		if(v == null) {
			try {
				res.sendError(404, "Video not found");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		String fileName = v.getPath().substring(v.getPath().lastIndexOf('\\')+1, v.getPath().length());
		return fileController.downloadFile(fileName, req);
		
	}
}
