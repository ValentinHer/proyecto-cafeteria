package com.valentin.reservacion_citas.web.dto.request;

import java.util.List;

public class WebhookCalendlySubscriptionReqDto {
	private String url;
	private List<String> events;
	private String organization;
	private String user;
	private String scope;

	public WebhookCalendlySubscriptionReqDto() {
	}

	public WebhookCalendlySubscriptionReqDto(String url, List<String> events, String organization, String user, String scope) {
		this.url = url;
		this.events = events;
		this.organization = organization;
		this.user = user;
		this.scope = scope;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> events) {
		this.events = events;
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

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "WebhookCalendlySubscriptionReqDto{" +
				"url='" + url + '\'' +
				", events=" + events +
				", organization='" + organization + '\'' +
				", user='" + user + '\'' +
				", scope='" + scope + '\'' +
				'}';
	}
}
