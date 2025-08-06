package com.valentin.reservacion_citas.web.config;

import com.valentin.reservacion_citas.domain.service.implementation.OAuth2UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

import java.util.Arrays;

@Configuration
public class SecurityConfig {
	private final JwtFilter jwtFilter;

	public SecurityConfig(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
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
						.requestMatchers("/webhooks/calendars/**").permitAll()
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
						.authenticationEntryPoint(((request, response, authException) -> {
							/*if (SecurityContextHolder.getContext().getAuthentication() == null) {
								response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
							} else {
								response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
							}*/

							response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

						}))
						.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
						}))
				.oauth2Login(oauth -> oauth
						.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
						.successHandler(oAuthSuccessHandler)
						.failureHandler(oAuth2FailureHandler)
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login")
						.deleteCookies("token")
						.invalidateHttpSession(false));

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
