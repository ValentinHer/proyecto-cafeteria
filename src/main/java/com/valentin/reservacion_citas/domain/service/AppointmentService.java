package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.request.GuestReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;

public interface AppointmentService {
	MessageResDto createAppointment(GuestReqDto guestReqDto);

    public MsgDataResDto getAllAppointments();
}
