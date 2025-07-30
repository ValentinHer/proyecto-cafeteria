package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.request.UserLoginReqDto;

public interface AuthService {
	String login(UserLoginReqDto userLoginReqDto);
}
