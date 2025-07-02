package com.valentin.reservacion_citas.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookPayloadReqDto {

	@JsonProperty("email")
	private String email;

	@JsonProperty("name")
	private String name;

	@JsonProperty("scheduled_event")
	private WebhookPayloadScheduledEventReqDto scheduledEvent;

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

	public WebhookPayloadScheduledEventReqDto getScheduledEvent() {
		return scheduledEvent;
	}

	public void setScheduledEvent(WebhookPayloadScheduledEventReqDto scheduledEvent) {
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
