package com.drupal.controllers;

//import java.io.IOException;

//import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drupal.StudentRestApiApplication;
import com.drupal.dao.InstituteRepo;
import com.drupal.dao.TokenRepo;
import com.drupal.dao.UserRepo;
import com.drupal.models.Institute;
import com.drupal.models.Token;
import com.drupal.models.User;

@Controller
public class TokenController {

	@Autowired
	TokenRepo tokenRepo;
	
	@Autowired 
	UserRepo userRepo;
	
	@Autowired
	InstituteRepo instituteRepo;

	/**Creates a new token for the new session of the user with id-userId.
	 * <p>
	 * The newly created token is associated with the user having id-userId.   
	 * 
	 * @param userId  user is uniquely identified by the userId.   
	 * @return Token object created for the new session of the user with id-userId.
	 */
	@PostMapping("/tokens/create")
	@ResponseBody
	public Token createToken(@RequestPart String userId) {
		Token newToken = new Token(userId);
		tokenRepo.save(newToken);
		return newToken;
	}

	/**
	 * Deletes the token with id:- id
	 * 
	 * @param id id of the token to be deleted.  
	 * @param email Used to identify the user associated with token having id as id. 
	 * @return Returns true if token is successfully deleted else in case of some error
	 * returns false. 
	 */
	@DeleteMapping("/tokens/delete")
	@ResponseBody
	public boolean deleteToken(@RequestPart String id, String email) {
		
			User user = userRepo.findByEmail(email);
			if (user == null || !validateToken(id, user.getId())) {
				Institute ins = instituteRepo.findByEmail(email);
				if(ins==null || !validateToken(id, ins.getId())) {
					return false;
				}
				tokenRepo.deleteById(id);
				return true;
			} else {
				tokenRepo.deleteById(id);
				return true;
			}
	}

	/**
	 * Finds the userId associated with the tokenId.
	 * 
	 * @param tokenId  The id of the token used to find the userId. 
	 * @return Returns the userId associated with the tokenId.
	 */
	public String getUserIdFrom(String tokenId) {
		Token tok = tokenRepo.findById(tokenId).orElse(null);
		if(tok==null) {
			return StudentRestApiApplication.NOT_FOUND;
		}
		return tok.getUserId();
	}
	
	
	/**
	 * Validates the token 
	 * <p>
	 * Accepts token and email as input parameters and determines if the token is valid or not.
	 *  
	 * @param token The token to be validated 
	 * @param email The email of the user in the current session.
	 * @return Returns true if token is valid else if token is expired return false.
	 */
	boolean validateToken(Token token, @NotNull String email) {
		String userId = token.getUserId();
		return validateToken(token.getId(), userId);
	}

	/**
	 * Validates the token
	 * <p>
	 * Accepts tokenId and userId as input parameters and determines if the token is valid or not.
	 * @param tokenId The id of token to be validated
	 * @param userId The id of user in the current session
	 * @return Returns true if token is valid else if token is expired return false.
	 */
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
