package com.valentin.reservacion_citas.web.config;

import com.valentin.reservacion_citas.domain.service.implementation.OAuth2UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	private final OAuth2UserServiceImpl oAuth2UserService;

	public SecurityConfig(OAuth2UserServiceImpl oAuth2UserService) {
		this.oAuth2UserService = oAuth2UserService;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request -> request.anyRequest().permitAll())
				.oauth2Login(oauth -> oauth
						.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
						.successHandler()
				);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
