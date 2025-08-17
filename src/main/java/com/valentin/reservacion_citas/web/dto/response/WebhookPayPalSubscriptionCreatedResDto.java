package com.valentin.reservacion_citas.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WebhookPayPalSubscriptionCreatedResDto {
	@JsonProperty("id")
	private String id;

	@JsonProperty("url")
	private String url;

	@JsonProperty("event_types")
	private List<Object> eventTypes;

	@JsonProperty("links")
	private List<Object> links;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Object> getEventTypes() {
		return eventTypes;
	}

	public void setEventTypes(List<Object> eventTypes) {
		this.eventTypes = eventTypes;
	}

	public List<Object> getLinks() {
		return links;
	}

	public void setLinks(List<Object> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "WebhookPayPalSubscriptionCreatedResDto{" +
				"id='" + id + '\'' +
				", url='" + url + '\'' +
				", eventTypes=" + eventTypes +
				", links=" + links +
				'}';
	}
}
