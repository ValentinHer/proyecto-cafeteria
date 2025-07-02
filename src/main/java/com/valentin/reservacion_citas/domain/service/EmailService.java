package com.valentin.reservacion_citas.domain.service;

import jakarta.mail.MessagingException;

public interface EmailService {
	void sendEmail() throws MessagingException;
}