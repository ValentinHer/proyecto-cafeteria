package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.service.NotificationService;
import com.valentin.reservacion_citas.web.dto.request.EmailReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.exception.InternalServerException;
import com.valentin.reservacion_citas.web.util.EmailSender;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class NotificationServiceImpl implements NotificationService {
	private final EmailSender emailSender;
	private final TemplateEngine templateEngine;
	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

	@Value("${spring.mail.username}")
	private String emailSendTo;

	@Autowired
	public NotificationServiceImpl(EmailSender emailSender, TemplateEngine templateEngine) {
		this.emailSender = emailSender;
		this.templateEngine = templateEngine;
	}

	private void sendConfirmationContactEmailToUser(EmailReqDto emailReqDto, Context context) {
		context.setVariable("usuario", emailReqDto.getName());

		String htmlContent = templateEngine.process("confirmationContactEmail", context);

		try {
			emailSender.sendEmail(htmlContent, emailReqDto.getEmail(), "Gracias por contactarnos – Hemos recibido tu mensaje");
			logger.info("Correo de confirmación de contacto enviado de forma exitosa al email {} del usuario {}", emailReqDto.getEmail(), emailReqDto.getName());
		} catch (MessagingException e) {
			logger.error("No se pudo enviar el correo de confirmación de contacto al email {} del usuario {}", emailReqDto.getEmail(), emailReqDto.getName(), e);
		}
	}

	@Override
	public MessageResDto sendContactEmailToOwner(EmailReqDto emailReqDto) {
		Context context = new Context();
		context.setVariable("nombre", emailReqDto.getName());
		context.setVariable("correo", emailReqDto.getEmail());
		context.setVariable("mensaje", emailReqDto.getMessage());

		String htmlContent = templateEngine.process("contactOwner", context);

		try {
			emailSender.sendEmail(htmlContent, emailSendTo, "Mensaje desde el sitio web Cafe Klang");
			logger.info("Correo enviado de forma exitosa al email {} para realizar contacto por parte del usuario {} con email {}", emailSendTo, emailReqDto.getName(), emailReqDto.getEmail());

			sendConfirmationContactEmailToUser(emailReqDto, context);

			return new MessageResDto("Correo enviado de forma exitosa", HttpStatus.OK.value());
		} catch (MessagingException e) {
			logger.error("No se pudo enviar el correo al email {} para realizar contacto por parte del usuario {} con email {}", emailSendTo, emailReqDto.getName(), emailReqDto.getEmail(), e);
			throw new InternalServerException("El Correo no se pudo enviar, por favor intente de nuevo");
		}
	}

	@Override
	public void sendAppointmentConfirmationEmail(String userName, String userEmail, String appointmentDate, String appointmentHour) {
		Context context = new Context();
		context.setVariable("nombre", userName);
		context.setVariable("fecha", appointmentDate);
		context.setVariable("hora", appointmentHour);

		String htmlContent = templateEngine.process("emailEventCreated", context);

		try {
			emailSender.sendEmail(htmlContent, userEmail, "Reservación Confirmada");
			logger.info("Correo enviado de forma exitosa al usuario {} con email {}", userName, userEmail);
		} catch (MessagingException e) {
			logger.error("No se pudo enviar correo al usuario {} con email {}", userName, userEmail, e);
		}
	}

	public void sendEmailToRestorePassword(String userName, String userEmail) {
		Context context = new Context();
		context.setVariable("nombre", userName);

		String htmlContent = templateEngine.process("restorePassword", context);

		try {
			emailSender.sendEmail(htmlContent, userEmail, "Restaurar contraseña");
			logger.info("Correo enviado de forma exitosa al usuario {} con email {}", userName, userEmail);
		} catch (MessagingException e) {
			logger.error("No se pudo enviar correo al usuario {} con email {}", userName, userEmail, e);
		}
	}
}
