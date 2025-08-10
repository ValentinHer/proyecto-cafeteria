package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.web.dto.request.AuthProviderReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserChangePasswordReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;

public interface UserService {
	User createUser(UserReqDto userReqDto);

	MessageResDto createUserWithEmailPassword(UserReqDto userReqDto, AuthProviderReqDto authProviderReqDto);

	void createUserWithGoogle(UserReqDto userReqDto, AuthProviderReqDto authProviderReqDto);

	User findByEmail(String email);

	MessageResDto restoreUserPassword(String email);

	MessageResDto changeUserPassword(UserChangePasswordReqDto userChangePasswordReqDto);
}
