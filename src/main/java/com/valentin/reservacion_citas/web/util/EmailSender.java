package com.valentin.reservacion_citas.web.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class EmailSender {

    private final String sendGridKey;

    private final SendGrid sendGrid;

    @Value("${sendgrid.sender}")
    private final String sender;

	private final JavaMailSenderImpl mailSender;

	public EmailSender(@Value("${sendgrid.api.key}") String sendGridKey, String sender, JavaMailSenderImpl mailSender) {
        this.sendGridKey = sendGridKey;
        this.sendGrid = new SendGrid(sendGridKey);
        this.sender = sender;
        this.mailSender = mailSender;
	}

	/*public void sendEmail(String html, String sendEmailTo, String subject) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(sendEmailTo);
		helper.setSubject(subject);
		helper.setText(html, true);

		ClassPathResource image = new ClassPathResource("/static/image (2).png");
		helper.addInline("logoImagen", image);

		mailSender.send(message);
	}*/

    public void sendEmail(String html, String sendEmailTo, String subject) throws IOException {

        Email from = new Email(sender); // remitente autorizado en SendGrid
        Email to = new Email(sendEmailTo);

        // Cargar imagen inline
        ClassPathResource image = new ClassPathResource("/static/image (2).png");
        byte[] imageBytes = image.getInputStream().readAllBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // Agregar imagen inline al HTML
        String htmlWithImage = html.replace("cid:logoImagen", "cid:logoImagen");

        Content content = new Content("text/html", htmlWithImage);
        Mail mail = new Mail(from, subject, to, content);

        // Crear adjunto inline
        Attachments attachments = new Attachments();
        attachments.setContent(base64Image);
        attachments.setType("image/png");
        attachments.setFilename("logo.png");
        attachments.setDisposition("inline");
        attachments.setContentId("logoImagen");

        mail.addAttachments(attachments);

        // Enviar request
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sendGrid.api(request);
    }
}
