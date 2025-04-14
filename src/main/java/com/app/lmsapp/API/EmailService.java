package com.app.lmsapp.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;

	public void SendEmail(String name, String email, String message) {

		String subject = "Welcome to Hand to Hand Notes";
		String hello = "Name is :" + name + "\n E-mail is :" + email + "\n Message is :" + message;
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		msg.setSubject(subject);
		msg.setText(hello);
		mailSender.send(msg);

	}
}
