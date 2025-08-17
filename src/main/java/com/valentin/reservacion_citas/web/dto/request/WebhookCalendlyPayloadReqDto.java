package com.valentin.reservacion_citas.web.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookCalendlyPayloadReqDto {

	@JsonProperty("email")
	private String email;

	@JsonProperty("name")
	private String name;

	@JsonProperty("scheduled_event")
	private WebhookCalendlyPayloadScheduledAppointmentReqDto scheduledEvent;

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

	public WebhookCalendlyPayloadScheduledAppointmentReqDto getScheduledEvent() {
		return scheduledEvent;
	}

	public void setScheduledEvent(WebhookCalendlyPayloadScheduledAppointmentReqDto scheduledEvent) {
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
