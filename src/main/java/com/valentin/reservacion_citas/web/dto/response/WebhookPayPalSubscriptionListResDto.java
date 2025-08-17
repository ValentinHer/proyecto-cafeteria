package com.valentin.reservacion_citas.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WebhookPayPalSubscriptionListResDto {
	@JsonProperty("webhooks")
	private List<WebhookPayPalSubscriptionCreatedResDto> webhooks;

	public List<WebhookPayPalSubscriptionCreatedResDto> getWebhooks() {
		return webhooks;
	}

	public void setWebhooks(List<WebhookPayPalSubscriptionCreatedResDto> webhooks) {
		this.webhooks = webhooks;
	}

	@Override
	public String toString() {
		return "WebhookPayPalSubscriptionListResDto{" +
				"webhooks=" + webhooks +
				'}';
	}
}
