package com.valentin.reservacion_citas.web.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
	private final JavaMailSenderImpl mailSender;

	public EmailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public void sendEmail(String html, String sendEmailTo, String subject) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(sendEmailTo);
		helper.setSubject(subject);
		helper.setText(html, true);

		ClassPathResource image = new ClassPathResource("/static/image (2).png");
		helper.addInline("logoImagen", image);

		mailSender.send(message);
	}
}
