package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.UserService;
import com.valentin.reservacion_citas.persistence.entity.AuthProviders;
import com.valentin.reservacion_citas.web.dto.request.AuthProviderReqDto;
import com.valentin.reservacion_citas.web.dto.request.UserReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
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
		authProviderReqDto.setEmail(userReqDto.getEmail());

		MessageResDto response = userService.createUserWithEmailPassword(userReqDto, authProviderReqDto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
