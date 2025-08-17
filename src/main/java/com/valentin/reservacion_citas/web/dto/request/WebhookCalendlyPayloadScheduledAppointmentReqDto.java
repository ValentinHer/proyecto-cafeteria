package com.valentin.reservacion_citas.web.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookCalendlyPayloadScheduledAppointmentReqDto {

	@JsonProperty("name")
	private String name;

	@JsonProperty("status")
	private String status;

	@JsonProperty("start_time")
	private String startTime;

	@JsonProperty("end_time")
	private String endTime;

	@JsonProperty("created_at")
	private String createdAt;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getStartTime() {
		return getDatetimeFormattedToCurrentZone(startTime);
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return getDatetimeFormattedToCurrentZone(endTime);
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public LocalDateTime getCreatedAt() {
		return getDatetimeFormattedToCurrentZone(createdAt);
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getDatetimeFormattedToCurrentZone(String datetime) {
		ZonedDateTime zonedDateTime = ZonedDateTime.parse(datetime);
		ZonedDateTime currentZoneWithDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("America/Mexico_City"));

		return currentZoneWithDateTime.toLocalDateTime();
	}

	@Override
	public String toString() {
		return "WebhookPayloadScheduledEventReqDto{" +
				"name='" + name + '\'' +
				", status='" + status + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", createdAt='" + createdAt + '\'' +
				'}';
	}
}
