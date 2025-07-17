package com.valentin.reservacion_citas.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EmailReqDto {
	@NotBlank(message = "El nombre es obligatorio")
	private String name;

	@NotBlank(message = "El email es obligatorio")
	private String email;

	@NotBlank(message = "El mensaje es obligatorio")
	private String message;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		try {
			return new String(Base64.getDecoder().decode(email), StandardCharsets.UTF_8);
		} catch (IllegalArgumentException e) {
			return email;
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "EmailReqDto{" +
				"name='" + name + '\'' +
				", email='" + getEmail() + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
