package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.AuthService;
import com.valentin.reservacion_citas.web.dto.request.UserLoginReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageJwtResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

	@Operation(summary = "Realizar un login de usuario", description = "Crear un login en base a las credenciales propocionados por el usuario")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Login exitoso"),
			@ApiResponse(responseCode = "401", description = "Credenciales inválidas")
	})
	@PostMapping("/login")
	public ResponseEntity<MessageJwtResDto> login(@RequestBody UserLoginReqDto userLoginReqDto) {
		String token = authService.login(userLoginReqDto);

		return new ResponseEntity<>(new MessageJwtResDto("Login realizado de forma exitosa", token, HttpStatus.OK.value()), HttpStatus.OK);
	}

	@Operation(summary = "Realizar logout de usuario", description = "Realizar un logout de usuario")
	@ApiResponse(responseCode = "200", description = "Logout realizado de forma exitosa")
	@PostMapping("/logout")
	public ResponseEntity<MessageResDto> logout() {
		return new ResponseEntity<>(new MessageResDto("Logout realizado de forma exitosa", HttpStatus.OK.value()), HttpStatus.OK);
	}
}
