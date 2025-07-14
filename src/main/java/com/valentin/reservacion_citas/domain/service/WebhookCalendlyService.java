package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.request.WebhookAppointmentCreatedReqDto;

import java.io.IOException;

public interface WebhookCalendlyService {
	public MessageResDto getOrCreateSubscription() throws IOException, InterruptedException;

	public MessageResDto handleAppointmentCreated(WebhookAppointmentCreatedReqDto payload);
}
