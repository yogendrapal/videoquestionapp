package com.drupal.controllers;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drupal.dao.TokenRepo;
import com.drupal.models.Token;

@Controller
public class TokenController {

	@Autowired
	TokenRepo tokenRepo;

	@PostMapping("/tokens/create")
	@ResponseBody
	public Token createToken(@RequestPart String email) {
		Date date = new Date();
		Token newToken = new Token(email);
		tokenRepo.save(newToken);
		return newToken;
	}

	@DeleteMapping("/tokens/delete")
	@ResponseBody
	public void deleteToken(@RequestPart String id, String email, HttpServletResponse res) {
		try {
			res.setContentType("application/json");
			if (!validateToken(id, email)) {
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

	boolean validateToken(Token token, @NotNull String email) {
		String tokenEmail = token.getEmail();
		if (email.equals(tokenEmail)) {
			return true;
		}
		return false;
	}

	boolean validateToken(@NotNull String tokenId, @NotNull String email) {
		Token token = tokenRepo.findById(tokenId).orElse(null);
		if (token == null) {
			System.out.println("validateToken: Token" + tokenId + " is not present");
			return false;
		}
		if (token.getEmail().equals(email)) {
			return true;
		}
		return false;
	}

}
