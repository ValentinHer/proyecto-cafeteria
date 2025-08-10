package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.mapper.AppointmentMapper;
import com.valentin.reservacion_citas.domain.mapper.GuestMapper;
import com.valentin.reservacion_citas.domain.mapper.UserMapper;
import com.valentin.reservacion_citas.domain.service.AppointmentService;
import com.valentin.reservacion_citas.persistence.entity.Appointment;
import com.valentin.reservacion_citas.persistence.entity.Guest;
import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.persistence.repository.AppointmentRepository;
import com.valentin.reservacion_citas.persistence.repository.GuestRepository;
import com.valentin.reservacion_citas.persistence.repository.UserRepository;
import com.valentin.reservacion_citas.web.dto.request.AppointmentReqDto;
import com.valentin.reservacion_citas.web.dto.request.GuestReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	private final UserRepository userRepository;
	private final GuestRepository guestRepository;
	private final AppointmentRepository appointmentRepository;
	private final GuestMapper guestMapper;
	private final AppointmentMapper appointmentMapper;
	private final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

	public AppointmentServiceImpl(UserRepository userRepository, GuestRepository guestRepository, AppointmentRepository appointmentRepository, GuestMapper guestMapper, AppointmentMapper appointmentMapper) {
		this.userRepository = userRepository;
		this.guestRepository = guestRepository;
		this.appointmentRepository = appointmentRepository;
		this.guestMapper = guestMapper;
		this.appointmentMapper = appointmentMapper;
	}

	@Override
	@Transactional
	public MessageResDto createAppointment(GuestReqDto guestReqDto) {
		Optional<User> userFound = userRepository.findByEmail(guestReqDto.getEmail());

		if(userFound.isEmpty()){

			Optional<Guest> guestFound = guestRepository.findByEmail(guestReqDto.getEmail());

			if (guestFound.isEmpty()) {
				Guest guest = guestMapper.toEntity(guestReqDto);
				Guest guestSaved = guestRepository.save(guest);

				logger.info("Reservación creada e invitado creado {} con email {} de forma exitosa", guestSaved.getName(), guestSaved.getEmail());
				return new MessageResDto("Reservación creada de forma exitosa", HttpStatus.CREATED.value());
			}

			return createAppointmentWithExistGuest( guestReqDto.getAppointments().getFirst(), guestFound.get());
		}

		return createAppointmentWithExistUser(guestReqDto.getAppointments().getFirst(), userFound.get());
	}

	public MessageResDto createAppointmentWithExistUser(AppointmentReqDto appointmentReqDto, User user) {
		Appointment appointment = appointmentMapper.toEntity(appointmentReqDto);
		appointment.setUser(user);

		Appointment appointmentSaved = appointmentRepository.save(appointment);

		logger.info("Reservación creada y asignada al usuario {} con email {}", user.getName(), user.getEmail());
		return new MessageResDto("Reservación creada de forma exitosa", HttpStatus.CREATED.value());
	}

	public MessageResDto createAppointmentWithExistGuest(AppointmentReqDto appointmentReqDto, Guest guest) {
		Appointment appointment = appointmentMapper.toEntity(appointmentReqDto);
		appointment.setGuest(guest);

		Appointment appointmentSaved = appointmentRepository.save(appointment);

		logger.info("Reservación creada y asignada al invitado {} con email {}", guest.getName(), guest.getEmail());
		return new MessageResDto("Reservación creada de forma exitosa", HttpStatus.CREATED.value());
	}
}
