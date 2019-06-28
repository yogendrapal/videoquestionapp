package com.drupal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailSenderService")
public class EmailSenderService {

    /**
     * A JavaMailSender automatically provided by Spring boot
     * 
     * This mail sender is used to send verification mails
     */
    private JavaMailSender javaMailSender;

    /**
     * Initializes an EmailSenderService.
     * 
     * Initializes a javaMailSender.
     * javaMailSender is automatically provided by Spring boot
     * 
     * @param javaMailSender {@link org.springframework.mail.javamail.JavaMailSender} which actually sends the mail
     */
    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sends an email as described by <i>email</i>.
     * 
     * The email object contains all details of the mail.
     * 
     * @param email the {@link org.springframework.mail.SimpleMailMessage} to send
     */
    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
}