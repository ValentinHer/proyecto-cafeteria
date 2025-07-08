package com.valentin.reservacion_citas.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class WebhookCalendlyListSubscriptionsResDto {

	@JsonProperty("collection")
	private List<WebhookCalendlySubscriptionResDto> collection;

	@JsonProperty("pagination")
	private Map<String, Object> pagination;

	public List<WebhookCalendlySubscriptionResDto> getCollection() {
		return collection;
	}

	public void setCollection(List<WebhookCalendlySubscriptionResDto> collection) {
		this.collection = collection;
	}

	public Map<String, Object> getPagination() {
		return pagination;
	}

	public void setPagination(Map<String, Object> pagination) {
		this.pagination = pagination;
	}

	@Override
	public String toString() {
		return "WebhookCalendlyListSubscriptionsResDto{" +
				"collection=" + collection +
				", pagination=" + pagination +
				'}';
	}
}
