package com.valentin.reservacion_citas.web.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookPayloadReqDto {

	@JsonProperty("email")
	private String email;

	@JsonProperty("name")
	private String name;

	@JsonProperty("scheduled_event")
	private WebhookPayloadScheduledAppointmentReqDto scheduledEvent;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WebhookPayloadScheduledAppointmentReqDto getScheduledEvent() {
		return scheduledEvent;
	}

	public void setScheduledEvent(WebhookPayloadScheduledAppointmentReqDto scheduledEvent) {
		this.scheduledEvent = scheduledEvent;
	}

	@Override
	public String toString() {
		return "WebhookPayloadReqDto{" +
				"email='" + email + '\'' +
				", name='" + name + '\'' +
				", scheduledEvent=" + scheduledEvent +
				'}';
	}
}
