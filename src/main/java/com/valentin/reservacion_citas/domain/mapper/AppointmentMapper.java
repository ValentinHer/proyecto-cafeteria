package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.Appointment;
import com.valentin.reservacion_citas.web.dto.request.AppointmentReqDto;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {
	public Appointment toEntity(AppointmentReqDto appointmentReqDto) {
		Appointment appointment = new Appointment();
		appointment.setStartTime(appointmentReqDto.getStartTime());
		appointment.setEndTime(appointmentReqDto.getEndTime());
		appointment.setStatus(appointmentReqDto.getStatus());
		appointment.setCreatedAt(appointmentReqDto.getCreatedAt());

		return appointment;
	}
}
