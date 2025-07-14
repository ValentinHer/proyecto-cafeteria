package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.Appointment;
import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
	private final AppointmentMapper appointmentMapper;

	public UserMapper(AppointmentMapper appointmentMapper) {
		this.appointmentMapper = appointmentMapper;
	}

	public User toEntity(UserReqDto userReqDto) {
		User user = new User();
		user.setName(userReqDto.getName());
		user.setEmail(userReqDto.getEmail());
		user.setAppointments(userReqDto.getAppointments().stream().map(appointmentReqDto -> {
			Appointment appointment = appointmentMapper.toEntity(appointmentReqDto);
			appointment.setUser(user);

			return appointment;
		}).toList());

		return user;
	}
}
