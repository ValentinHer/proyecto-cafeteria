package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.UserService;
import com.valentin.reservacion_citas.persistence.entity.AuthProviders;
import com.valentin.reservacion_citas.web.dto.request.AuthProviderReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserChangePasswordReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserRestorePasswordReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
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

	@PostMapping
	public ResponseEntity<MessageResDto> create(@RequestBody UserReqDto userReqDto) {
		AuthProviderReqDto authProviderReqDto = new AuthProviderReqDto();
		authProviderReqDto.setName(AuthProviders.EMAIL);

		MessageResDto response = userService.createUserWithEmailPassword(userReqDto, authProviderReqDto);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@PostMapping("/restore-password")
	public ResponseEntity<MessageResDto> restorePassword(@RequestBody @Valid UserRestorePasswordReqDto userRestorePasswordReqDto) {
		return new ResponseEntity<>(userService.restoreUserPassword(userRestorePasswordReqDto.getEmail()), HttpStatus.OK);
	}

	@PostMapping("/change-password")
	public ResponseEntity<MessageResDto> changePassword(@RequestBody @Valid UserChangePasswordReqDto userChangePasswordReqDto) {
		return new ResponseEntity<>(userService.changeUserPassword(userChangePasswordReqDto), HttpStatus.OK);
	}
}
