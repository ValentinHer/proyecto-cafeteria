package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.service.AuthService;
import com.valentin.reservacion_citas.web.dto.request.UserLoginReqDto;
import com.valentin.reservacion_citas.web.exception.InvalidCredentialException;
import com.valentin.reservacion_citas.web.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public String login(UserLoginReqDto userLoginReqDto) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginReqDto.getEmail(), userLoginReqDto.getPassword());
			authenticationManager.authenticate(authenticationToken);

			return jwtUtil.createJwt(userLoginReqDto.getEmail());
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialException("Credenciales inválidas");
		}
	}
}
