package com.drupal.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drupal.AES;
import com.drupal.StudentRestApiApplication;
import com.drupal.dao.UserRepo;
import com.drupal.models.User;
import com.drupal.models.Token;

@Controller
public class ApiController {

	@Autowired
	TokenController tokenController;

	@Autowired
	UserController userController;

	@Autowired
	UserRepo userRepo;

	@RequestMapping(path = "login", method=RequestMethod.POST)
	@ResponseBody
	public String login(@RequestPart String email, @RequestPart String password, HttpServletResponse res) {
		User user = userRepo.findByEmail(email);
		if (user != null) {
			if (AES.encrypt(password, StudentRestApiApplication.SECRET_KEY).equals(user.getPassword())) {
				Token token = tokenController.createToken(email);
				return token.getId();
			} else {
				try {
					res.sendError(400, "Incorrect password");
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

	@RequestMapping(path="signup", method=RequestMethod.POST)
	@ResponseBody
	public String signUp(@RequestPart String email, @RequestPart String password, @RequestPart String name,  HttpServletResponse res) {
		User user = userController.postUser(name, email, password);
		return login(email, password, res);
	}
}
