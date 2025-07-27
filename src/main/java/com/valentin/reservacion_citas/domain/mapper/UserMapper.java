package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.Appointment;
import com.valentin.reservacion_citas.persistence.entity.Guest;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
	private final AppointmentMapper appointmentMapper;

	public UserMapper(AppointmentMapper appointmentMapper) {
		this.appointmentMapper = appointmentMapper;
	}

	public Guest toEntity(UserReqDto userReqDto) {
		Guest guest = new Guest();
		guest.setName(userReqDto.getName());
		guest.setEmail(userReqDto.getEmail());
		guest.setAppointments(userReqDto.getAppointments().stream().map(appointmentReqDto -> {
			Appointment appointment = appointmentMapper.toEntity(appointmentReqDto);
			appointment.setGuest(guest);

			return appointment;
		}).toList());

		return guest;
	}
}
