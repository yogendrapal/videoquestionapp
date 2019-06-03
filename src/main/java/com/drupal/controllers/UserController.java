package com.drupal.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drupal.AES;
import com.drupal.StudentRestApiApplication;
import com.drupal.dao.TokenRepo;
import com.drupal.dao.UserRepo;
import com.drupal.models.Token;
import com.drupal.models.User;
import com.drupal.models.UserHiddenPassword;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class UserController {
	@Autowired
	UserRepo repo;
	
	@Autowired
	TokenRepo tokenRepo;

	@RequestMapping(path = "users", method = RequestMethod.GET)
	@ResponseBody
	public List<UserHiddenPassword> getAllUsers() {
		List<User> users= repo.findAll();
		List<UserHiddenPassword> usersHiddenPassword = new ArrayList<UserHiddenPassword>();
		for(User user: users) {
			usersHiddenPassword.add(new UserHiddenPassword(user));
		}
		return usersHiddenPassword;
		
	}

	@RequestMapping(path = "users/create", method = RequestMethod.POST)
	@ResponseBody
	public User postUser(@RequestPart String name, @RequestPart String email, @RequestPart String password) {
		System.out.println("inside post");
		String encryptedPass = AES.encrypt(password, StudentRestApiApplication.SECRET_KEY);
		User user = new User(name, email, encryptedPass);
		System.out.println(user.getId());
		repo.save(user);
		return user;
	}

	@RequestMapping(path = "users/update/{id}", method = RequestMethod.PUT, produces = {"appliation/json"})
	@ResponseBody
	public String saveOrUpdateUser(@PathVariable("id") String id,  @RequestPart(name="name") String name, @RequestPart String email, @RequestPart String password , @RequestPart String tokenId , HttpServletResponse res) {
		Token token = tokenRepo.findById(tokenId).orElse(null);
		if(token == null)
			return "Error";
		String userId = token.getUserId();
		User user = repo.findById(id).orElse(null);
		System.out.println("inside put");
		if(user==null) {
			return "{\"Error\":\"User assiciated with the id is not present\"}";
		}
		
		if(userId.equals(user.getId())) {
			user.setEmail(email);
			user.setName(name);
			String encryptedPass = AES.encrypt(password, StudentRestApiApplication.SECRET_KEY);
			user.setPassword(encryptedPass);
			repo.save(user);
			
			ObjectMapper Obj = new ObjectMapper(); 
			String jsonStr = user.toString();
	        try { 
	            jsonStr = Obj.writeValueAsString(user); 
	            System.out.println(jsonStr); 
	        } 
	  
	        catch (IOException e) { 
	            e.printStackTrace(); 
	        } 
			// return "Home"; This also works
			return jsonStr;
		}
		
		//System.out.println("Error in updating");
		return "{\"Error\" : \"You are not allowed do this\"}";
		
	}

	
	//Something Wrong
	@RequestMapping(path = "users/{id}", method = RequestMethod.GET)
	@ResponseBody
	public UserHiddenPassword getUser(@PathVariable("id") String id) {
		System.out.println(repo.findById(id));
		User user = repo.findById(id).orElse(null);
		UserHiddenPassword toReturn = new UserHiddenPassword(user);
		if(user==null) {
			return null;
		}
		return toReturn;
	}

	
	// TODO token authentication for deletion
	@DeleteMapping(path = "users/delete/{id}")
	@ResponseBody
	public String deleteUser(@PathVariable("id") String id ,  @RequestPart String tokenId) {
		Token token = tokenRepo.findById(tokenId).orElse(null);
		if(token == null)
			return "{\"Error\" : \"No such token\"}";
		String userId = token.getUserId();
		User user = repo.findById(id).orElse(null);
		if (user != null) {
			if(userId.equals(user.getId())){
				repo.deleteById(id);
				tokenRepo.deleteById(token.getId());
				return "Deleted";
			}else {
				return "{\"Error\" : \"You are not allowed do this\"}";
			}
		} else {
			return "{\"Error\" : \"User Not Present\"}";
		}
	}
}
