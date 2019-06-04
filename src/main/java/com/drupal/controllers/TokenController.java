package com.drupal.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drupal.StudentRestApiApplication;
import com.drupal.dao.TokenRepo;
import com.drupal.dao.UserRepo;
import com.drupal.models.Token;
import com.drupal.models.User;

@Controller
public class TokenController {

	@Autowired
	TokenRepo tokenRepo;
	
	@Autowired 
	UserRepo userRepo;

	@PostMapping("/tokens/create")
	@ResponseBody
	public Token createToken(@RequestPart String userId) {
		Token newToken = new Token(userId);
		tokenRepo.save(newToken);
		return newToken;
	}

	@DeleteMapping("/tokens/delete")
	@ResponseBody
	public void deleteToken(@RequestPart String id, String userId, HttpServletResponse res) {
		try {
			res.setContentType("application/json");
			if (!validateToken(id, userId)) {
				res.setStatus(403);
				res.getWriter().write("{\"Error\":\"Token is invalid\"}");
			} else {
				tokenRepo.deleteById(id);
				res.setStatus(200);
				res.getWriter().write("{\"Success\":\"Token successfully deleted\"}");
			}
			res.getWriter().flush();
			res.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getUserIdFrom(String tokenId) {
		Token tok = tokenRepo.findById(tokenId).orElse(null);
		if(tok==null) {
			return StudentRestApiApplication.NOT_FOUND;
		}
		return tok.getUserId();
	}
	
	boolean validateToken(Token token, @NotNull String email) {
		String userId = token.getUserId();
		return validateToken(token.getId(), userId);
	}

	boolean validateToken(@NotNull String tokenId, @NotNull String userId) {
		Token token = tokenRepo.findById(tokenId).orElse(null);
		if (token == null) {
			return false;
		}
		if (token.getUserId().equals(userId)) {
			return true;
		}
		return false;
	}

}
