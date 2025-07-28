package com.valentin.reservacion_citas.web.dto.request;

import java.util.List;

public class GuestReqDto {
	private String name;
	private String email;
	private List<AppointmentReqDto> appointments;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<AppointmentReqDto> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<AppointmentReqDto> appointments) {
		this.appointments = appointments;
	}

	@Override
	public String toString() {
		return "UserReqDto{" +
				"name='" + name + '\'' +
				", email='" + email + '\'' +
				", appointments=" + appointments +
				'}';
	}
}
