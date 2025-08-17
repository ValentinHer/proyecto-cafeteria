package com.valentin.reservacion_citas.web.dto.request;

import java.util.List;
import java.util.Map;

public class WebhookPayPalCreateSubscriptionReqDto {
	private String url;
	private List<Map<String, String>> event_types;

	public WebhookPayPalCreateSubscriptionReqDto() {
	}

	public WebhookPayPalCreateSubscriptionReqDto(String url, List<Map<String, String>> event_types) {
		this.url = url;
		this.event_types = event_types;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Map<String, String>> getEvent_types() {
		return event_types;
	}

	public void setEvent_types(List<Map<String, String>> event_types) {
		this.event_types = event_types;
	}
}
