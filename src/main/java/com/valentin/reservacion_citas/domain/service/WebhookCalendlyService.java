package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.request.WebhookAppointmentCreatedReqDto;


public interface WebhookCalendlyService {
	void handleAppointmentCreated(WebhookAppointmentCreatedReqDto payload);
}
