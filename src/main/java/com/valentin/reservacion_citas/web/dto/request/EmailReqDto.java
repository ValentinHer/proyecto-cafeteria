package com.valentin.reservacion_citas.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailReqDto {
	@NotBlank(message = "El nombre es obligatorio")
	private String name;

	@NotBlank(message = "El email es obligatorio")
	@Email(message = "Correo inválido")
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
		return email;
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
				", email='" + email + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
