package com.valentin.reservacion_citas.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valentin.reservacion_citas.domain.service.implementation.OAuth2UserServiceImpl;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
	private final JwtFilter jwtFilter;
	private final ObjectMapper objectMapper;

	public SecurityConfig(JwtFilter jwtFilter, ObjectMapper objectMapper) {
		this.jwtFilter = jwtFilter;
		this.objectMapper = objectMapper;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, OAuth2UserServiceImpl oAuth2UserService, OAuthSuccessHandler oAuthSuccessHandler, OAuth2FailureHandler oAuth2FailureHandler) throws Exception {
		http
				.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(request -> request
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/emails/**").permitAll()
						.requestMatchers("/webhooks/**").permitAll()
						.requestMatchers("/swagger-ui/**").permitAll()
						.requestMatchers("/v3/api-docs/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/categories/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PATCH, "/categories/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
						.requestMatchers("/categories/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/users/**").permitAll()
						.requestMatchers("/orders").hasAnyRole("ADMIN", "USER")
						.requestMatchers(HttpMethod.GET, "/products").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/products/**").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint((request, response, authException) -> {
							MessageResDto messageResDto = new MessageResDto(authException.getMessage(), HttpStatus.UNAUTHORIZED.value());
							String res = objectMapper.writeValueAsString(messageResDto);

							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.setContentType("application/json;charset=UTF-8");
							response.getWriter().write(res);
						})
						.accessDeniedHandler((request, response, accessDeniedException) -> {
							MessageResDto messageResDto = new MessageResDto("No tienes permisos para realizar esta acción", HttpStatus.FORBIDDEN.value());
							String res = objectMapper.writeValueAsString(messageResDto);

							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
							response.setContentType("application/json;charset=UTF-8");
							response.getWriter().write(res);
						}))
				.oauth2Login(oauth -> oauth
						.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
						.successHandler(oAuthSuccessHandler)
						.failureHandler(oAuth2FailureHandler)
				);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
