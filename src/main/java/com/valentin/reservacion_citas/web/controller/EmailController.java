package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class EmailController {
	private final EmailService emailService;

	@Autowired
	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping("/send")
	public void sendEmail() throws MessagingException {
		emailService.sendEmail();
	}
}
