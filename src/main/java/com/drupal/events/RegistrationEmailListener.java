package com.drupal.events;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.drupal.StudentRestApiApplication;
import com.drupal.dao.InstituteRepo;
import com.drupal.dao.UserRepo;
import com.drupal.dao.VerificationTokenRepo;
import com.drupal.models.VerificationToken;

/**
 * This listener listen for a OnRegistrationSuccessEvent and uses a MailSender to send email verification mail if event occurs.
 * 
 * @author pratik, sai, henil, shweta
 * @see OnRegistrationSuccessEvent
 * @see org.springframework.mail.MailSender
 */
@Component
public class RegistrationEmailListener implements ApplicationListener<OnRegistrationSuccessEvent> {

	@Value("${env.ip}")
	private String ip;
	
	@Value("${env.port}")
	private String port;
	
	@Autowired
	private MailSender mailSender;

	@Autowired
	private VerificationTokenRepo verificationTokenRepo;

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	InstituteRepo instituteRepo;

	/**
	 * Handler for handling the event.
	 * 
	 * This method doesn't do anything directly but instead
	 * calls confirmRegistration() method on this event
	 *
	 * @param event the event which is thrown on successful registration/Creation of a User/Institute account
	 * @see confirmRegistration 
	 * @see com.drupal.models.User
	 * @see com.drupal.models.Institute
	 */
	@Override
	public void onApplicationEvent(OnRegistrationSuccessEvent event) {
		this.confirmRegistration(event);
	}

	/**
	 * Sends an email-verification-mail using the information in event
	 * 
	 * It performs the following tasks-
	 * <ol>
	 * <li> Creates a VerificationToken in the database for this event
	 * <li> Extracts email from the event and sends a verification mail containing the verification-token
	 * </ol>
	 * 
	 * @param event the event which is thrown on successful registration/Creation of a User/Institute account
	 * @see org.springframework.mail.MailSender
	 * @see com.drupal.models.VerificationToken
	 */
	private void confirmRegistration(OnRegistrationSuccessEvent event) {
		String tokenId = UUID.randomUUID().toString();
		VerificationToken token = new VerificationToken(event.getUserId(), tokenId);
		verificationTokenRepo.save(token);

		String message = "Click on this link to confirm your registration:\n";
		String trailingMessage = " \nThe link will expire after 24 hours";
		SimpleMailMessage email = new SimpleMailMessage();
		System.out.println(email.toString());
		if(event.type == StudentRestApiApplication.RegistrationTypes.user ) {
			email.setTo(userRepo.findById(event.getUserId()).orElse(null).getEmail());
		}
		else {
		
			email.setTo(instituteRepo.findById(event.getUserId()).orElse(null).getEmail());	
		}
		System.out.println("AFTER INSTITUTE WAS FOUND");
		System.out.println(email.toString());
		email.setSubject("Confirm registration to videoquestion app");
		email.setText(message + "http://"+ip+":"+port+"/confirmtoken?type="+event.type+"&token=" + tokenId + trailingMessage);
		try {
			mailSender.send(email);
		} catch (Exception e) {
			System.out.println("Email sending failed exception ocured");
			System.out.println(e.toString());
			userRepo.deleteById(event.getUserId());
			verificationTokenRepo.delete(verificationTokenRepo.findByToken(tokenId));
			throw new MailException("Can't send verification mail") {
				private static final long serialVersionUID = 2L;
			};
		}
	}
}
