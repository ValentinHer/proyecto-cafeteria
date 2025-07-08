package com.valentin.reservacion_citas.web.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookAppointmentCreatedReqDto {

	@JsonProperty("payload")
	WebhookPayloadReqDto payload;

	public WebhookPayloadReqDto getPayload() {
		return payload;
	}

	public void setPayload(WebhookPayloadReqDto payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "WebhookEventCreatedReqDto{" +
				"payload=" + payload +
				'}';
	}
}
