package com.valentin.reservacion_citas.web.config;

import com.valentin.reservacion_citas.domain.service.implementation.UserDetailsServiceImpl;
import com.valentin.reservacion_citas.web.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

	public JwtFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// Validar que haya una cookie válida
		Cookie[] cookies = request.getCookies();

		if(cookies == null || cookies.length < 1){
			filterChain.doFilter(request, response);
			return;
		}

		Optional<Cookie> cookieFiltered = Arrays.stream(cookies)
												.filter(cookie -> cookie.getName()
																		.equals("token"))
												.findFirst();

		if (cookieFiltered.isEmpty()) {
			filterChain.doFilter(request, response);
			return;
		}

		Cookie cookie = cookieFiltered.get();

		String jwt = cookie.getValue();

		// Validar que el token sea válido
		if (!jwtUtil.validJwt(jwt)) {
			filterChain.doFilter(request, response);
			return;
		}

		// Cargar el usuario del UserDetailsService
		String userEmail = jwtUtil.getUsername(jwt);
		User user = (User) userDetailsService.loadUserByUsername(userEmail);

		// Cargar al usuario en el contexto de seguridad
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				user, null,user.getAuthorities()
		);

		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		filterChain.doFilter(request, response);
	}
}
