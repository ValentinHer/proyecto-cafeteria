package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.UserService;
import com.valentin.reservacion_citas.persistence.entity.AuthProviders;
import com.valentin.reservacion_citas.web.dto.request.AuthProviderReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserChangePasswordReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserRestorePasswordReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "Registrar usuario", description = "Registrar un usuario")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Usuario registrado"),
			@ApiResponse(responseCode = "409", description = "Usuario con email existente")
	})
	@PostMapping
	public ResponseEntity<MessageResDto> create(@RequestBody UserReqDto userReqDto) {
		AuthProviderReqDto authProviderReqDto = new AuthProviderReqDto();
		authProviderReqDto.setName(AuthProviders.EMAIL);

		MessageResDto response = userService.createUserWithEmailPassword(userReqDto, authProviderReqDto);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@Operation(summary = "Enviar email para restaurar contraseña", description = "Enviar email para restaurar contraseña")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Email enviado para restaurar contraseña"),
			@ApiResponse(responseCode = "404", description = "Usuario no encontrado")
	})
	@PostMapping("/restore-password")
	public ResponseEntity<MessageResDto> restorePassword(@RequestBody @Valid UserRestorePasswordReqDto userRestorePasswordReqDto) {
		return new ResponseEntity<>(userService.restoreUserPassword(userRestorePasswordReqDto.getEmail()), HttpStatus.OK);
	}

	@Operation(summary = "Cambiar contraseña del usuario", description = "Cambiar contraseña del usuario")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Contraseña cambiada"),
			@ApiResponse(responseCode = "404", description = "Usuario no encontrado")
	})
	@PostMapping("/change-password")
	public ResponseEntity<MessageResDto> changePassword(@RequestBody @Valid UserChangePasswordReqDto userChangePasswordReqDto) {
		return new ResponseEntity<>(userService.changeUserPassword(userChangePasswordReqDto), HttpStatus.OK);
	}
}
