package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.request.EmailReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;

public interface NotificationService {
	MessageResDto sendContactEmailToOwner(EmailReqDto emailReqDto);

	void sendAppointmentConfirmationEmail(String userName, String userEmail, String appointmentDate, String appointmentHour);

	void sendEmailToRestorePassword(String userName, String userEmail);
}
