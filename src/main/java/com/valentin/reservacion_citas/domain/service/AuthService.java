package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.request.UserLoginReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;

public interface AuthService {
	String login(UserLoginReqDto userLoginReqDto);
}
