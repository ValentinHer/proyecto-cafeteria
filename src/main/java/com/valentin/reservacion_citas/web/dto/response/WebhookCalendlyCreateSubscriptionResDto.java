package com.valentin.reservacion_citas.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookCalendlyCreateSubscriptionResDto {
	@JsonProperty("resource")
	private WebhookCalendlySubscriptionResDto resource;

	public WebhookCalendlySubscriptionResDto getResource() {
		return resource;
	}

	public void setResource(WebhookCalendlySubscriptionResDto resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "WebhookCalendlyCreateSubscriptionResDto{" +
				"resource=" + resource +
				'}';
	}
}
