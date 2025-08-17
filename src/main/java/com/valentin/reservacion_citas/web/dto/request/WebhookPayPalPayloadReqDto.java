package com.valentin.reservacion_citas.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookPayPalPayloadReqDto {

	@JsonProperty("id")
	private String id;

	@JsonProperty("create_time")
	private String createTime;

	@JsonProperty("resource_type")
	private String resourceType;

	@JsonProperty("event_type")
	private String eventType;

	@JsonProperty("resource")
	private WebhookPayPalResourcePayloadReqDto resource;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public WebhookPayPalResourcePayloadReqDto getResource() {
		return resource;
	}

	public void setResource(WebhookPayPalResourcePayloadReqDto resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "WebhookPayPalPayloadReqDto{" +
				"id='" + id + '\'' +
				", createTime='" + createTime + '\'' +
				", resourceType='" + resourceType + '\'' +
				", eventType='" + eventType + '\'' +
				", resource=" + resource +
				'}';
	}
}
