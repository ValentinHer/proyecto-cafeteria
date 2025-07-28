package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.persistence.entity.AuthProvider;
import com.valentin.reservacion_citas.web.dto.request.AuthProviderReqDto;

public interface AuthProviderService {
	AuthProvider create(AuthProviderReqDto authProviderReqDto, String userId);
}
