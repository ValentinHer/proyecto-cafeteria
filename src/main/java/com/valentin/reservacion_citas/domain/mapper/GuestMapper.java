package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.Guest;
import com.valentin.reservacion_citas.web.dto.request.GuestReqDto;
import org.springframework.stereotype.Component;

@Component
public class GuestMapper {
	private final AppointmentMapper appointmentMapper;

	public GuestMapper(AppointmentMapper appointmentMapper) {
		this.appointmentMapper = appointmentMapper;
	}

	public Guest toEntity(GuestReqDto guestReqDto) {
		Guest guest = new Guest();
		guest.setEmail(guestReqDto.getEmail());
		guest.setName(guestReqDto.getName());
		guest.setAppointments(guestReqDto.getAppointments().stream().map(appointmentMapper::toEntity).toList());
		guest.setIsActive(true);

		return guest;
	}
}
