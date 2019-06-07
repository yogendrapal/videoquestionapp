package com.drupal.events;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.drupal.dao.UserRepo;
import com.drupal.dao.VerificationTokenRepo;
import com.drupal.models.VerificationToken;

@Component
public class RegistrationEmailListener implements ApplicationListener<OnRegistrationSuccessEvent> {

	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private VerificationTokenRepo verificationTokenRepo;
	
	@Autowired
	UserRepo userRepo;

	@Override
	public void onApplicationEvent(OnRegistrationSuccessEvent event) {
		this.confirmRegistration(event);
	}

	
	private void confirmRegistration(OnRegistrationSuccessEvent event) {
		String tokenId = UUID.randomUUID().toString();
		VerificationToken token = new VerificationToken(event.getUserId(), tokenId);
		verificationTokenRepo.save(token);
		
		String message = "Click on this link to confirm your registration:\n";
		String trailingMessage = " \nThe link will expire after 24 hours";
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(userRepo.findById(event.getUserId()).orElse(null).getEmail());
		email.setSubject("Confirm registration to videoquestion app");
		email.setText(message + "http://192.168.43.244:8080/confirmtoken?token="+tokenId + trailingMessage);
		mailSender.send(email);
	}
}