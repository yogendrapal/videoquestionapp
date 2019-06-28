package com.drupal.controllers;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drupal.StudentRestApiApplication;
import com.drupal.dao.InstituteRepo;
import com.drupal.events.OnRegistrationSuccessEvent;
import com.drupal.models.Institute;
import com.drupal.models.User;
import com.drupal.services.AES;


@Controller
public class InstituteController {
	
	@Autowired
	InstituteRepo instituteRepo;
	
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	/**
	 * Creates a new Institute user.
	 * <p>
	 * Uses postUser() method to create a new Institute user.
	 * 
	 * @param name The name of institute
	 * @param address The address of the institute
	 * @param email The email of the institute
	 * @param password The password of institute
	 * @param phone The phone number of institute
	 * @param res The response that is sent to the client
	 * @return A string which describes whether sign-up was successful or not and depending 
	 * on the string an appropriate message is shown on the client side.
	 */
	@RequestMapping(path="institute/create", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String signupAsInstitute(@RequestPart String name, @RequestPart String address,@RequestPart String email, @RequestPart String password, @RequestPart String phone,HttpServletResponse res) {
		if (instituteRepo.findByEmail(email) == null) {
			System.out.println("inside signup");
			Institute inst = postUser(name, address ,email, password, phone);
			System.out.println(inst.getId());
			eventPublisher.publishEvent(new OnRegistrationSuccessEvent(inst.getId(),StudentRestApiApplication.RegistrationTypes.institute));
			return "{\"Success\" : \"User created successfully\"}";
//			return login(email, password, res);
		} else {
			try {
				res.sendError(400, "User with this email already exists");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "{\"Error\" : \"User with this email already exists\"}";
		}
	}
	
	/**
	 * Helper function called by signupAsInstitute() to create a new Institute user
	 * <p> 
	 *  Creates a new Institute user and adds the institute's information such as name,address,
	 *  email,password(in encrypted form) and phone number into the database.
	 *  
	 * @param name The name of institute
	 * @param address The address of the institute
	 * @param email The email of the institute
	 * @param password The password of institute
	 * @param phone The phone number of institute
	 * @return The new institute object created after registration. 
	 */
	@ResponseBody
	public Institute postUser(@RequestPart String name, @RequestPart String address,@RequestPart String email, @RequestPart String password, @RequestPart String phone) {
		System.out.println("inside users post");
		
		String encryptedPass = AES.encrypt(password, StudentRestApiApplication.SECRET_KEY);
		Institute inst = new Institute(name, address, phone, encryptedPass,email, null);
		instituteRepo.save(inst);
		System.out.println((inst.getId()));
		eventPublisher.publishEvent(new OnRegistrationSuccessEvent(inst.getId(),StudentRestApiApplication.RegistrationTypes.institute));
		return inst;
	}
	
	
	/**
	 * Finds Institute on the basis of the email.
	 * <p>
	 * Uses email to get all the details of the institute namely name,email,address 
	 * and the phone number.
	 *    
	 * @param email The email of the institute to be found
	 * @param res The response that is sent to the client
	 * @return The JSON response consisting of the details of the institute  
	 */
	@RequestMapping(path="/getInstituteDetails")
	@ResponseBody
	public String getInstituteDetails(@RequestParam String email, HttpServletResponse res) {
		Institute inst = instituteRepo.findByEmail(email);
		if(inst==null) {
			try {
				res.sendError(400, "User with this email doesn't exist");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "{\"Error\":\"User with this email does't exist\"}";
		}
		else {
			return "{\"Name\":\""+ inst.getName()+"\",\"Email\":\""+inst.getEmail()+"\", \"Addresss\":\""+inst.getAddress()+"\",\"Phone\":\""+inst.getPhone()+"\"}";
		}
	}
	
	
}
