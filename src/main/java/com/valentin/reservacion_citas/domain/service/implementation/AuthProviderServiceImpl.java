package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.mapper.AuthProviderMapper;
import com.valentin.reservacion_citas.domain.service.AuthProviderService;
import com.valentin.reservacion_citas.persistence.entity.AuthProvider;
import com.valentin.reservacion_citas.persistence.repository.AuthProviderRepository;
import com.valentin.reservacion_citas.web.dto.request.AuthProviderReqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthProviderServiceImpl implements AuthProviderService {
	private final AuthProviderRepository authProviderRepository;
	private final AuthProviderMapper authProviderMapper;
	private final Logger logger = LoggerFactory.getLogger(AuthProviderServiceImpl.class);

	public AuthProviderServiceImpl(AuthProviderRepository authProviderRepository, AuthProviderMapper authProviderMapper) {
		this.authProviderRepository = authProviderRepository;
		this.authProviderMapper = authProviderMapper;
	}


	@Override
	public AuthProvider create(AuthProviderReqDto authProviderReqDto, String userId) {
		AuthProvider authProvider = authProviderMapper.toEntity(authProviderReqDto);
		authProvider.setUserId(userId);

		AuthProvider authProviderSaved = authProviderRepository.save(authProvider);
		logger.info("AuthProvider creado del usuario: id={}", authProviderSaved.getUserId());

		return authProviderSaved;
	}
}
