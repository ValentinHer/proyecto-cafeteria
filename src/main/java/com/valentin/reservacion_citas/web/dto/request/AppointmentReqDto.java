package com.valentin.reservacion_citas.web.dto.request;

import com.valentin.reservacion_citas.persistence.entity.AppointmentStatus;

import java.time.LocalDateTime;

public class AppointmentReqDto {
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private AppointmentStatus status;
	private LocalDateTime createdAt;

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "AppointmentReqDto{" +
				"startTime=" + startTime +
				", endTime=" + endTime +
				", status=" + status +
				", createdAt=" + createdAt +
				'}';
	}
}
