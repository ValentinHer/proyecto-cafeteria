package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.mapper.AppointmentMapper;
import com.valentin.reservacion_citas.domain.mapper.UserMapper;
import com.valentin.reservacion_citas.domain.service.AppointmentService;
import com.valentin.reservacion_citas.persistence.entity.Appointment;
import com.valentin.reservacion_citas.persistence.entity.Guest;
import com.valentin.reservacion_citas.persistence.repository.AppointmentRepository;
import com.valentin.reservacion_citas.persistence.repository.GuestRepository;
import com.valentin.reservacion_citas.web.dto.request.AppointmentReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	private final GuestRepository guestRepository;
	private final AppointmentRepository appointmentRepository;
	private final UserMapper userMapper;
	private final AppointmentMapper appointmentMapper;
	private final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

	public AppointmentServiceImpl(GuestRepository guestRepository, AppointmentRepository appointmentRepository, UserMapper userMapper, AppointmentMapper appointmentMapper) {
		this.guestRepository = guestRepository;
		this.appointmentRepository = appointmentRepository;
		this.userMapper = userMapper;
		this.appointmentMapper = appointmentMapper;
	}

	@Override
	@Transactional
	public MessageResDto createAppointment(UserReqDto userReqDto) {
		Optional<Guest> userFound = guestRepository.findByEmail(userReqDto.getEmail());

		if(userFound.isEmpty()){
			Guest guest = userMapper.toEntity(userReqDto);
			Guest guestSaved = guestRepository.save(guest);

			logger.info("Cita creada y usuario creado {} con email {} de forma exitosa", guestSaved.getName(), guestSaved.getEmail());
			return new MessageResDto("Cita creada de forma exitosa", HttpStatus.CREATED.value());
		}

		return createAppointmentWithExistUser(userReqDto.getAppointments().getFirst(), userFound.get());
	}

	public MessageResDto createAppointmentWithExistUser(AppointmentReqDto appointmentReqDto, Guest guest) {
		Appointment appointment = appointmentMapper.toEntity(appointmentReqDto);
		appointment.setGuest(guest);

		Appointment appointmentSaved = appointmentRepository.save(appointment);

		logger.info("Cita creada y asignada al usuario {} con email {}", guest.getName(), guest.getEmail());
		return new MessageResDto("Cita creada de forma exitosa", HttpStatus.CREATED.value());
	}
}
