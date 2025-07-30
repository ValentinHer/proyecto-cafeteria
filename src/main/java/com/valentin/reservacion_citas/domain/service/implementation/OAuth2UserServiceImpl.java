package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.service.UserService;
import com.valentin.reservacion_citas.persistence.entity.AuthProviders;
import com.valentin.reservacion_citas.web.dto.request.AuthProviderReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import com.valentin.reservacion_citas.web.exception.ConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final UserService userService;
	private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
	private final Logger logger = LoggerFactory.getLogger(OAuth2UserServiceImpl.class);

	public OAuth2UserServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		if (!"google".equals(registrationId)){
			logger.error("Error al loguearse con el proveedor {}", registrationId);
			throw new OAuth2AuthenticationException("Proveedor OAuth2 no soportado: " + registrationId);
		}

		try {
			handleCreateUser(oAuth2User);

			return oAuth2User;
		} catch (ConflictException ce) {
			logger.warn("Conflicto al crear usuario con Google: {}", ce.getMessage());
			throw new OAuth2AuthenticationException(new OAuth2Error("conflict"), ce.getMessage());
		}
	}

	private void handleCreateUser(OAuth2User oAuth2User) {
		Map<String, Object> user = oAuth2User.getAttributes();
		logger.info("Datos del usuario: {}", user.toString());

		String sub = oAuth2User.getAttribute("sub");
		String name = oAuth2User.getAttribute("name");
		String email = oAuth2User.getAttribute("email");

		UserReqDto userReqDto = new UserReqDto();
		userReqDto.setName(name);
		userReqDto.setEmail(email);

		AuthProviderReqDto authProviderReqDto = new AuthProviderReqDto();
		authProviderReqDto.setName(AuthProviders.GOOGLE);
		authProviderReqDto.setProviderUserId(sub);

		userService.createUserWithGoogle(userReqDto, authProviderReqDto);
	}
}
