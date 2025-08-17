package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.request.WebhookPayPalPayloadReqDto;

public interface WebhookPayPalService {
	void handlePaymentCompleted(WebhookPayPalPayloadReqDto payload);
}
