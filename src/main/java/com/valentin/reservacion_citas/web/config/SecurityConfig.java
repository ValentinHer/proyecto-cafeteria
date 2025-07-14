package com.valentin.reservacion_citas.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request -> request.anyRequest().permitAll());

		return http.build();
	}
}
