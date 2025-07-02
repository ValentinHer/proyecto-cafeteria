package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.MessageResDto;
import com.valentin.reservacion_citas.web.dto.WebhookEventCreatedReqDto;

import java.io.IOException;

public interface WebhookCalendlyService {
	public MessageResDto getOrCreateSubscription() throws IOException, InterruptedException;

	public MessageResDto handleEventCreated(WebhookEventCreatedReqDto payload);
}
