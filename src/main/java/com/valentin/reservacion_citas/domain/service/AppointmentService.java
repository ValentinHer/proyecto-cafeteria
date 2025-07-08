package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;

public interface AppointmentService {
	MessageResDto createAppointment(UserReqDto userReqDto);
}
