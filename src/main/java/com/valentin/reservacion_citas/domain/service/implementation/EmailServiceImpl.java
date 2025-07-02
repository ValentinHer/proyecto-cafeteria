package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	private final JavaMailSenderImpl mailSender;

	@Autowired
	public EmailServiceImpl(JavaMailSenderImpl sender) {
		this.mailSender = sender;
	}

	@Override
	public void sendEmail() throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo("mxvalentin1822@gmail.com");
		helper.setSubject("Test prueba envio email con HTML");
		helper.setText("""
							       <html>
							           <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
							               <div style="background-color: #ffffff; border-radius: 8px; padding: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
							                   <h1 style="color: #333333;">Hola, usuario!</h1>
							                   <p style="color: #666666;">Este es un correo con estilos en línea enviados desde Spring Boot.</p>
							                   <a href="https://tusitio.com" style="color: white; background-color: #007BFF; padding: 10px 15px; text-decoration: none; border-radius: 4px;">Visitar sitio</a>
							               </div>
							           </body>
							       </html>
						""", true);

		mailSender.send(message);
	}
}
