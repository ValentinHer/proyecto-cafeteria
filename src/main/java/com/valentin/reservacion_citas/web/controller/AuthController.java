package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.AuthService;
import com.valentin.reservacion_citas.web.dto.request.UserLoginReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageJwtResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<MessageJwtResDto> login(@RequestBody UserLoginReqDto userLoginReqDto, HttpServletResponse response) {
		String token = authService.login(userLoginReqDto);

		return new ResponseEntity<>(new MessageJwtResDto("Login realizado de forma exitosa", token, HttpStatus.OK.value()), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<MessageResDto> logout(HttpServletResponse response) {
		return new ResponseEntity<>(new MessageResDto("Logout realizado de forma exitosa", HttpStatus.OK.value()), HttpStatus.OK);
	}
}
