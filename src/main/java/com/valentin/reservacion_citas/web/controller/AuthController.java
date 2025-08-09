package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.AuthService;
import com.valentin.reservacion_citas.web.dto.request.UserLoginReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import jakarta.servlet.http.Cookie;
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
	public ResponseEntity<MessageResDto> login(@RequestBody UserLoginReqDto userLoginReqDto, HttpServletResponse response) {
		String token = authService.login(userLoginReqDto);

		/*Cookie cookie = new Cookie("token", token);
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(3600);*/

		String cookieString = String.format("token=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Lax", token, 3600);
		response.addHeader("Set-Cookie", cookieString);

		return new ResponseEntity<>(new MessageResDto("Login realizado de forma exitosa", HttpStatus.OK.value()), HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<MessageResDto> logout(HttpServletResponse response) {
		Cookie cookie = new Cookie("token", null);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);

		return new ResponseEntity<>(new MessageResDto("Logout realizado de forma exitosa", HttpStatus.OK.value()), HttpStatus.OK);
	}
}
