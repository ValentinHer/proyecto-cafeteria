package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.AuthProvider;
import com.valentin.reservacion_citas.web.dto.request.AuthProviderReqDto;
import org.springframework.stereotype.Component;

@Component
public class AuthProviderMapper {
	public AuthProvider toEntity(AuthProviderReqDto authProviderReqDto) {
		AuthProvider authProvider = new AuthProvider();
		authProvider.setName(authProviderReqDto.getName());
		authProvider.setUserId(authProviderReqDto.getUserId());
		authProvider.setProviderUserId(authProviderReqDto.getProviderUserId());
		authProvider.setPassword(authProviderReqDto.getPassword());
		authProvider.setResetToken(authProviderReqDto.getResetToken());

		return authProvider;
	}
}
