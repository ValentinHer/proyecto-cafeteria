package com.valentin.reservacion_citas.domain.service.implementation;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final DefaultOAuth2UserService defaultOAuth2UserService;

	public OAuth2UserServiceImpl(DefaultOAuth2UserService defaultOAuth2UserService) {
		this.defaultOAuth2UserService = defaultOAuth2UserService;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

		String regitrationId = userRequest.getClientRegistration().getRegistrationId();

		if (!"google".equals(regitrationId)){
			throw new OAuth2AuthenticationException("Proveedor OAuth2 no soportado: " + regitrationId);
		}

		return null;
	}
}
