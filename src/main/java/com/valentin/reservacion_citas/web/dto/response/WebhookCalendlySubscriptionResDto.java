package com.valentin.reservacion_citas.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WebhookCalendlySubscriptionResDto {
	@JsonProperty("uri")
	private String uri;

	@JsonProperty("callback_url")
	private String callbackUrl;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("updated_at")
	private String updatedAt;

	@JsonProperty("retry_started_at")
	private String retryStartedAt;

	@JsonProperty("state")
	private String state;

	@JsonProperty("events")
	private List<String> events;

	@JsonProperty("scope")
	private String scope;

	@JsonProperty("organization")
	private String organization;

	@JsonProperty("user")
	private String user;

	@JsonProperty("group")
	private String group;

	@JsonProperty("creator")
	private String creator;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getRetryStartedAt() {
		return retryStartedAt;
	}

	public void setRetryStartedAt(String retryStartedAt) {
		this.retryStartedAt = retryStartedAt;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> events) {
		this.events = events;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public String toString() {
		return "WebhookCalendlySubscriptionResDto{" +
				"uri='" + uri + '\'' +
				", callbackUrl='" + callbackUrl + '\'' +
				", createdAt='" + createdAt + '\'' +
				", updatedAt='" + updatedAt + '\'' +
				", retryStartedAt='" + retryStartedAt + '\'' +
				", state='" + state + '\'' +
				", events=" + events +
				", scope='" + scope + '\'' +
				", organization='" + organization + '\'' +
				", user='" + user + '\'' +
				", group='" + group + '\'' +
				", creator='" + creator + '\'' +
				'}';
	}
}
